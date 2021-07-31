package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateTodoTest {

    @Mock
    private lateinit var repository: TodoRepository

    private lateinit var updateTodo: UpdateTodo

    @Before
    fun setUp() {
        updateTodo = UpdateTodo(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `updateTodo should return the updated todo when success`() = runBlockingTest {
        // given
        val id = "generated_id"
        val isDone = true
        val expectedTodo = Todo(id = id, name = "laundry", isDone = isDone)

        Mockito.`when`(repository.updateTodo(id, isDone)).thenReturn(ResultOf.Success(expectedTodo))

        // when
        val resultUpdateTodo = updateTodo(id, isDone) as ResultOf.Success
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
        Mockito.`when`(repository.updateTodo(id, isDone)).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null,
            )
        )

        // when
        val resultUpdateTodo = updateTodo(id, isDone)

        // then
        assertTrue(resultUpdateTodo is ResultOf.Failure)
    }
}

