package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteTodoTest {

    @Mock
    private lateinit var repository: TodoRepository

    private lateinit var deleteTodo: DeleteTodo

    @Before
    fun setUp() {
        deleteTodo = DeleteTodo(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return true when success`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(repository.deleteTodo(id)).thenReturn(ResultOf.Success(true))

        // when
        val resultDeletion = deleteTodo(id) as ResultOf.Success

        // then
        assertTrue(resultDeletion.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return false when todo is not deleted`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(repository.deleteTodo(id)).thenReturn(ResultOf.Success(false))

        // when
        val resultDeletion = deleteTodo(id) as ResultOf.Success

        // then
        assertFalse(resultDeletion.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteTodo should return failure`() = runBlockingTest {
        // given
        val id = "generated_id"

        Mockito.`when`(repository.deleteTodo(id)).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null
            )
        )

        // when
        val resultDeletion = deleteTodo(id)

        // then
        assertTrue(resultDeletion is ResultOf.Failure)
    }
}
