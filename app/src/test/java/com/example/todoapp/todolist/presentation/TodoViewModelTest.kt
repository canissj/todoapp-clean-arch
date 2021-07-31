package com.example.todoapp.todolist.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.core.usecases.GetAllTodos
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
    private lateinit var stateObserver: Observer<State>

    private lateinit var todoViewModel: TodoViewModel

    @Before
    fun setUp() {
        todoViewModel = TodoViewModel(getAllTodos)
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
                note = "remember",
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
        verify(stateObserver).onChanged(State.Loading)
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
        verify(stateObserver).onChanged(State.Loading)
        verify(stateObserver).onChanged(State.Error)
        verifyNoMoreInteractions(stateObserver)
    }
}
