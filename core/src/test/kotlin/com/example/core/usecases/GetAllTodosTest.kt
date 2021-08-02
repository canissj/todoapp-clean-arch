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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllTodosTest {

    @Mock
    private lateinit var repository: TodoRepository

    private lateinit var getAllTodos: GetAllTodos

    @Before
    fun setUp() {
        getAllTodos = GetAllTodos(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllTodos should return list of todos`() = runBlockingTest {
        // given
        val todo = Todo(id = "generated_id", name = "laundry")
        val todo2 = Todo(id = "generated_id2", name = "walk te dog")
        val todoList = listOf(todo, todo2)

        Mockito.`when`(repository.getAllTodos()).thenReturn(ResultOf.Success(todoList))

        // when
        val resultGetAllTodos = getAllTodos() as ResultOf.Success
        val resultTodos = resultGetAllTodos.value

        // then
        assertEquals(2, resultTodos.size)
        assertEquals(todo, resultTodos.first())
        assertEquals(todo2, resultTodos.last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllTodos should return failure`() = runBlockingTest {
        // given
        Mockito.`when`(repository.getAllTodos()).thenReturn(
            ResultOf.Failure(
                message = "some api error",
                throwable = null,
            )
        )

        // when
        val resultGetAllTodos = getAllTodos()

        // then
        assertTrue(resultGetAllTodos is ResultOf.Failure)
    }
}
