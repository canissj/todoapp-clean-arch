package com.example.todoapp.todolist.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.core.usecases.AddTodo
import com.example.core.usecases.GetAllTodos
import com.example.core.usecases.GetToken
import com.example.core.usecases.SignIn
import com.example.todoapp.todolist.presentation.util.CoroutineTestRule
import com.example.todoapp.todolist.presentation.util.LifeCycleTestOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class TodoViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getAllTodos: GetAllTodos

    @Mock
    private lateinit var signIn: SignIn

    @Mock
    private lateinit var getToken: GetToken

    @Mock
    private lateinit var addTodo: AddTodo

    @Mock
    private lateinit var stateObserver: Observer<State>

    @Mock
    private lateinit var toastObserver: Observer<in String?>

    private lateinit var todoViewModel: TodoViewModel

    @Before
    fun setUp() {
        todoViewModel = TodoViewModel(signIn, getAllTodos, addTodo, getToken)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `init should post ShowTodos without calling signIn when token is available`() =
        runBlockingTest {
            // given
            val lifeCycleTestOwner = LifeCycleTestOwner()
            val expectedTodos = listOf(
                Todo(
                    id = "id",
                    name = "walk the dog",
                    isDone = false
                )
            )
            val result = ResultOf.Success(expectedTodos)
            `when`(getAllTodos()).thenReturn(result)
            `when`(getToken()).thenReturn("token")

            // for testing init
            val localTodoViewModel = TodoViewModel(signIn, getAllTodos, addTodo, getToken)

            val stateLiveData = localTodoViewModel.state
            stateLiveData.observe(lifeCycleTestOwner, stateObserver)

            // when
            lifeCycleTestOwner.onResume()
            localTodoViewModel.getTodos()

            // then
            verify(stateObserver).onChanged(State.Loading)
            verify(stateObserver, times(2)).onChanged(State.ShowTodos(expectedTodos))
            verifyNoMoreInteractions(stateObserver)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `init should post ShowTodos and call signIn when token is not available`() =
        runBlockingTest {
            // given
            val lifeCycleTestOwner = LifeCycleTestOwner()
            val expectedTodos = listOf(
                Todo(
                    id = "id",
                    name = "walk the dog",
                    isDone = false
                )
            )
            val result = ResultOf.Success(expectedTodos)
            `when`(getAllTodos()).thenReturn(result)
            `when`(getToken()).thenReturn(null)
            `when`(signIn("user1")).thenReturn(ResultOf.Success(true))

            // when
            val localTodoViewModel = TodoViewModel(signIn, getAllTodos, addTodo, getToken)

            val stateLiveData = localTodoViewModel.state
            stateLiveData.observe(lifeCycleTestOwner, stateObserver)
            lifeCycleTestOwner.onResume()

            // then
            verify(stateObserver).onChanged(State.ShowTodos(expectedTodos))
            verify(signIn, times(2)).invoke("user1")
            verifyNoMoreInteractions(stateObserver)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `init should post RetryError when fail to signIn`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        `when`(getToken()).thenReturn(null)
        `when`(signIn("user1")).thenReturn(ResultOf.Failure())

        // for testing init
        val localTodoViewModel = TodoViewModel(signIn, getAllTodos, addTodo, getToken)

        val stateLiveData = localTodoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        // when
        lifeCycleTestOwner.onResume()

        // then
        verify(stateObserver).onChanged(State.ShowRetryError)
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getTodos should post ShowTodos state`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val expectedTodos = listOf(
            Todo(
                id = "id",
                name = "walk the dog",
                isDone = false
            )
        )
        val result = ResultOf.Success(expectedTodos)
        `when`(getAllTodos()).thenReturn(result)

        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.getTodos()

        // then
        verify(stateObserver, times(2)).onChanged(State.Loading)
        verify(stateObserver).onChanged(State.ShowTodos(expectedTodos))
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getTodos should post Error state`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val result = ResultOf.Failure()
        `when`(getAllTodos()).thenReturn(result)

        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.getTodos()

        // then
        verify(stateObserver, times(2)).onChanged(State.Loading)
        verify(stateObserver).onChanged(State.ShowRetryError)
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getTodos should post ShowTodos if todo cache is available`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val expectedTodos = listOf(
            Todo(
                id = "id",
                name = "walk the dog",
                isDone = false
            )
        )
        val successResult = ResultOf.Success(expectedTodos)
        val failureResult = ResultOf.Failure()

        `when`(getAllTodos())
            .thenReturn(successResult)
            .thenReturn(failureResult)

        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.getTodos() // success call to fill cache
        todoViewModel.getTodos() // failure call but should return cache

        // then
        verify(stateObserver, times(3)).onChanged(State.Loading)
        verify(stateObserver, times(2)).onChanged(State.ShowTodos(expectedTodos))
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should post ShowTodos with new todo when success`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val addedTodo =
            Todo(
                name = "walk the dog",
                isDone = false
            )
        val expectedTodo = addedTodo.copy(id = "generated_id")
        val expectedTodos = listOf(expectedTodo)

        `when`(addTodo(addedTodo)).thenReturn(ResultOf.Success(expectedTodo))

        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.addNewTodo("walk the dog")

        // then
        verify(stateObserver, times(2)).onChanged(State.Loading)
        verify(stateObserver).onChanged(State.ShowTodos(expectedTodos))
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should post showToast with error msg when failure`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val addedTodo =
            Todo(
                name = "walk the dog",
                isDone = false
            )

        `when`(addTodo(addedTodo)).thenReturn(ResultOf.Failure())

        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        val toastLiveData = todoViewModel.toastMessage
        toastLiveData.observe(lifeCycleTestOwner, toastObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.addNewTodo("walk the dog")

        // then
        verify(stateObserver, times(2)).onChanged(State.Loading)
        verify(stateObserver).onChanged(State.ShowTodos(listOf()))
        verify(toastObserver).onChanged("Could not add new todo")
        verifyNoMoreInteractions(stateObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should post toast message with error when todo is empty`() = runBlockingTest {
        // given
        val lifeCycleTestOwner = LifeCycleTestOwner()
        val stateLiveData = todoViewModel.state
        stateLiveData.observe(lifeCycleTestOwner, stateObserver)

        val toastLiveData = todoViewModel.toastMessage
        toastLiveData.observe(lifeCycleTestOwner, toastObserver)

        // when
        lifeCycleTestOwner.onResume()
        todoViewModel.addNewTodo("")

        // then
        verify(stateObserver).onChanged(State.Loading)
        verify(toastObserver).onChanged("Cannot add an empty todo")
        verifyNoMoreInteractions(stateObserver)
    }
}
