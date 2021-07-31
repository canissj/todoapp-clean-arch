package com.example.core.data

import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TodoRepositoryTest {

    @Mock
    private lateinit var todoDataSource: TodoDataSource

    private lateinit var repository: TodoRepository

    @Before
    fun setUp() {
        repository = TodoRepositoryImpl(todoDataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return the added todo with generated ID`() = runBlockingTest {
        // given
        val todo = Todo(name = "test", isDone = true)
        val expectedTodo = todo.copy(id = "generated_id")

        Mockito.`when`(todoDataSource.add(todo)).thenReturn(ResultOf.Success(expectedTodo))

        // when
        val resultTodo = repository.addTodo(todo) as ResultOf.Success

        // then
        Assert.assertEquals(expectedTodo, resultTodo.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return failure`() = runBlockingTest {
        // given
        val todo = Todo(name = "test", isDone = true)

        Mockito.`when`(todoDataSource.add(todo)).thenReturn(
            ResultOf.Failure(
                message = "fail",
                throwable = null
            )
        )

        // when
        val resultTodo = repository.addTodo(todo)

        // then
        Assert.assertTrue(resultTodo is ResultOf.Failure)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return true when success`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(todoDataSource.delete(id)).thenReturn(ResultOf.Success(true))

        // when
        val resultDeletion = repository.deleteTodo(id) as ResultOf.Success

        // then
        Assert.assertTrue(resultDeletion.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return false when todo is not deleted`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(todoDataSource.delete(id)).thenReturn(ResultOf.Success(false))

        // when
        val resultDeletion = repository.deleteTodo(id) as ResultOf.Success

        // then
        Assert.assertFalse(resultDeletion.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return failure`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(todoDataSource.delete(id)).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null
            )
        )

        // when
        val resultDeletion = repository.deleteTodo(id)

        // then
        Assert.assertTrue(resultDeletion is ResultOf.Failure)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllTodos should return list of todos`() = runBlockingTest {
        // given
        val todo = Todo(id = "generated_id", name = "laundry", isDone = true)
        val todo2 = Todo(id = "generated_id2", name = "walk te dog", isDone = false)
        val todoList = listOf(todo, todo2)

        Mockito.`when`(todoDataSource.getAll()).thenReturn(ResultOf.Success(todoList))

        // when
        val resultGetAllTodos = repository.getAllTodos() as ResultOf.Success
        val resultTodos = resultGetAllTodos.value

        // then
        Assert.assertEquals(2, resultTodos.size)
        Assert.assertEquals(todo, resultTodos.first())
        Assert.assertEquals(todo2, resultTodos.last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllTodos should return failure`() = runBlockingTest {
        // given
        Mockito.`when`(todoDataSource.getAll()).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null,
            )
        )

        // when
        val resultGetAllTodos = repository.getAllTodos()

        // then
        Assert.assertTrue(resultGetAllTodos is ResultOf.Failure)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `updateTodo should return the updated todo when success`() = runBlockingTest {
        // given
        val id = "generated_id"
        val isDone = true
        val expectedTodo = Todo(id = id, name = "laundry", isDone = isDone)

        Mockito.`when`(todoDataSource.update(id, isDone)).thenReturn(ResultOf.Success(expectedTodo))

        // when
        val resultUpdateTodo = repository.updateTodo(id, isDone) as ResultOf.Success
        val resultTodo = resultUpdateTodo.value

        // then
        Assert.assertEquals(expectedTodo, resultTodo)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `updateTodo should return failure`() = runBlockingTest {
        // given
        val id = "generated_id"
        val isDone = true
        Mockito.`when`(todoDataSource.update(id, isDone)).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null,
            )
        )

        // when
        val resultUpdateTodo = repository.updateTodo(id, isDone)

        // then
        Assert.assertTrue(resultUpdateTodo is ResultOf.Failure)
    }
}
