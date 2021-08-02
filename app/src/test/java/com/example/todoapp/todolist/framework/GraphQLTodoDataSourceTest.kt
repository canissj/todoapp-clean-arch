package com.example.todoapp.todolist.framework

import GetAllTasksQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.example.core.data.TodoDataSource
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.todoapp.todolist.framework.graphql.GraphQLTodoDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GraphQLTodoDataSourceTest {

    @Mock
    private lateinit var apolloClient: ApolloClient

    @Mock
    private lateinit var queryCall: ApolloQueryCall<GetAllTasksQuery.Data>

    @Captor
    private lateinit var captor: ArgumentCaptor<ApolloCall.Callback<GetAllTasksQuery.Data>>

    private lateinit var graphQlDataSource: TodoDataSource

    @Before
    fun setUp() {
        graphQlDataSource = GraphQLTodoDataSource(apolloClient)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return the list of todos`() = runBlockingTest {
        // given
        val expectedTodo = Todo(
            id = "generated_id",
            name = "walk the dog",
            isDone = true
        )
        val listOfTasks = listOf(
            GetAllTasksQuery.AllTask(
                id = "generated_id",
                name = "walk the dog",
                isDone = true,
                note = "note"
            )
        )
        val data = GetAllTasksQuery.Data(listOfTasks)
        val builder = Response.builder<GetAllTasksQuery.Data>(GetAllTasksQuery()).data(data)
        val response = Response(builder)

        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(captor.capture())).thenAnswer {
            captor.value.onResponse(response)
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Success
        val todos = getAllResult.value

        // then
        assertEquals(1, todos.size)
        assertEquals(expectedTodo, todos.first())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return failure when query has errors`() = runBlockingTest {
        // given
        val errors = listOf(Error("some_error"))
        val builder = Response.builder<GetAllTasksQuery.Data>(GetAllTasksQuery()).errors(errors)
        val response = Response(builder)

        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(captor.capture())).thenAnswer {
            captor.value.onResponse(response)
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.UnknownError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return failure when connectivity error`() = runBlockingTest {
        // given
        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(captor.capture())).thenAnswer {
            captor.value.onFailure(ApolloNetworkException("network_error"))
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.HttpConnectivityNetworkError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return failure when http error`() = runBlockingTest {
        // given
        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(captor.capture())).thenAnswer {
            captor.value.onFailure(ApolloHttpException(null))
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.HttpNetworkError)
    }
}
