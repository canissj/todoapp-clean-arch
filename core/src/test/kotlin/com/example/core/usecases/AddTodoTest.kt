package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddTodoTest {

    @Mock
    private lateinit var repository: TodoRepository

    private lateinit var addTodo: AddTodo

    @Before
    fun setUp() {
        addTodo = AddTodo(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return the added todo with generated ID`() = runBlockingTest {
        // given
        val todo = Todo(name = "test", isDone = true)
        val expectedTodo = todo.copy(id = "generated_id")

        `when`(repository.addTodo(todo)).thenReturn(ResultOf.Success(expectedTodo))

        // when
        val resultTodo = addTodo(todo) as ResultOf.Success

        // then
        assertEquals(expectedTodo, resultTodo.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return failure`() = runBlockingTest {
        // given
        val todo = Todo(name = "test", isDone = true)

        `when`(repository.addTodo(todo)).thenReturn(
            ResultOf.Failure(
                message = "fail",
                throwable = null
            )
        )

        // when
        val resultTodo = addTodo(todo)

        // then
        assertTrue(resultTodo is ResultOf.Failure)
    }
}
