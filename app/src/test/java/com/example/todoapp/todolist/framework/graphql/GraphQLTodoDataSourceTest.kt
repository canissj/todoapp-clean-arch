package com.example.todoapp.todolist.framework.graphql

import CreateTodoMutation
import GetAllTasksQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloHttpException
import com.example.core.data.TodoDataSource
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.todoapp.todolist.framework.DataSourceError
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

    @Mock
    private lateinit var mutationCall: ApolloMutationCall<CreateTodoMutation.Data>

    @Captor
    private lateinit var queryCaptor: ArgumentCaptor<ApolloCall.Callback<GetAllTasksQuery.Data>>

    @Captor
    private lateinit var mutationCaptor: ArgumentCaptor<ApolloCall.Callback<CreateTodoMutation.Data>>

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

        `when`(queryCall.enqueue(queryCaptor.capture())).thenAnswer {
            queryCaptor.value.onResponse(response)
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
    fun `getAll should return UnknownError when api error`() = runBlockingTest {
        // given
        val errors = listOf(Error("some_error"))
        val builder = Response.builder<GetAllTasksQuery.Data>(GetAllTasksQuery()).errors(errors)
        val response = Response(builder)

        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(queryCaptor.capture())).thenAnswer {
            queryCaptor.value.onResponse(response)
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.UnknownError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return AuthenticationError when auth api error`() = runBlockingTest {
        // given
        val errors = listOf(Error("Access token is not valid"))
        val builder = Response.builder<GetAllTasksQuery.Data>(GetAllTasksQuery()).errors(errors)
        val response = Response(builder)

        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(queryCaptor.capture())).thenAnswer {
            queryCaptor.value.onResponse(response)
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.AuthenticationError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAll should return HttpNetworkError when http error`() = runBlockingTest {
        // given
        `when`(apolloClient.query(any(GetAllTasksQuery::class.java))).thenReturn(queryCall)

        `when`(queryCall.enqueue(queryCaptor.capture())).thenAnswer {
            queryCaptor.value.onFailure(ApolloHttpException(null))
        }

        // when
        val getAllResult = graphQlDataSource.getAll() as ResultOf.Failure

        // then
        assertTrue(getAllResult.throwable is DataSourceError.HttpNetworkError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return new todo when success`() = runBlockingTest {
        // given
        val expectedTodo = Todo(
            id = "generated_id",
            name = "walk the dog",
            isDone = true
        )
        val createTask = CreateTodoMutation.CreateTask(
            id = "generated_id",
            name = "walk the dog",
            isDone = true,
        )

        val data = CreateTodoMutation.Data(createTask)
        val builder =
            Response.builder<CreateTodoMutation.Data>(CreateTodoMutation("", false)).data(data)
        val response = Response(builder)

        `when`(apolloClient.mutate(any(CreateTodoMutation::class.java))).thenReturn(mutationCall)

        `when`(mutationCall.enqueue(mutationCaptor.capture())).thenAnswer {
            mutationCaptor.value.onResponse(response)
        }

        // when
        val createTodoResult = graphQlDataSource.add(expectedTodo) as ResultOf.Success

        // then
        assertEquals(expectedTodo, createTodoResult.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return HttpNetworkError when api error`() = runBlockingTest {
        // given
        val expectedTodo = Todo(
            id = "generated_id",
            name = "walk the dog",
            isDone = true
        )

        `when`(apolloClient.mutate(any(CreateTodoMutation::class.java))).thenReturn(mutationCall)

        `when`(mutationCall.enqueue(mutationCaptor.capture())).thenAnswer {
            mutationCaptor.value.onFailure(ApolloHttpException(null))
        }

        // when
        val createTodoResult = graphQlDataSource.add(expectedTodo) as ResultOf.Failure

        // then
        assertTrue(createTodoResult.throwable is DataSourceError.HttpNetworkError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return UnknownError when api error`() = runBlockingTest {
        // given
        val expectedTodo = Todo(
            id = "generated_id",
            name = "walk the dog",
            isDone = true
        )

        val errors = listOf(Error("some_error"))
        val builder =
            Response.builder<CreateTodoMutation.Data>(CreateTodoMutation("", false)).errors(errors)
        val response = Response(builder)

        `when`(apolloClient.mutate(any(CreateTodoMutation::class.java))).thenReturn(mutationCall)

        `when`(mutationCall.enqueue(mutationCaptor.capture())).thenAnswer {
            mutationCaptor.value.onResponse(response)
        }

        // when
        val createTodoResult = graphQlDataSource.add(expectedTodo) as ResultOf.Failure

        // then
        assertTrue(createTodoResult.throwable is DataSourceError.UnknownError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addTodo should return AuthenticationError when auth api error`() = runBlockingTest {
        // given
        val expectedTodo = Todo(
            id = "generated_id",
            name = "walk the dog",
            isDone = true
        )

        val errors = listOf(Error("Access token is not valid"))
        val builder =
            Response.builder<CreateTodoMutation.Data>(CreateTodoMutation("", false)).errors(errors)
        val response = Response(builder)

        `when`(apolloClient.mutate(any(CreateTodoMutation::class.java))).thenReturn(mutationCall)

        `when`(mutationCall.enqueue(mutationCaptor.capture())).thenAnswer {
            mutationCaptor.value.onResponse(response)
        }

        // when
        val createTodoResult = graphQlDataSource.add(expectedTodo) as ResultOf.Failure

        // then
        assertTrue(createTodoResult.throwable is DataSourceError.AuthenticationError)
    }
}
