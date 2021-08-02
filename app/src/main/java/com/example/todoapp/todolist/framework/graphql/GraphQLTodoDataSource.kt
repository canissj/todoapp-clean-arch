package com.example.todoapp.todolist.framework.graphql

import CreateTodoMutation
import GetAllTasksQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.core.data.TodoDataSource
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.todoapp.todolist.framework.DataSourceError

class GraphQLTodoDataSource(private val apolloClient: ApolloClient) : TodoDataSource {

    private val authError = "Access token is not valid"

    override suspend fun getAll(): ResultOf<List<Todo>> {
        val errorMsg = "fail to fetch todos"
        val response = try {
            apolloClient
                .query(GetAllTasksQuery())
                .await()
        } catch (e: ApolloException) {
            return ResultOf.Failure(
                errorMsg,
                throwable = DataSourceError.HttpNetworkError(e.message)
            )
        }

        response.data?.allTasks?.let {
            val todos = NetworkMapper.toListOfTodos(it)
            return ResultOf.Success(todos)
        }

        if (authError.equals(response.errors?.first()?.message, ignoreCase = false)) {
            return ResultOf.Failure(
                message = errorMsg,
                throwable = DataSourceError.AuthenticationError(authError)
            )
        }

        return ResultOf.Failure(
            message = errorMsg,
            throwable = DataSourceError.UnknownError(response.errors?.first()?.message)
        )
    }

    override suspend fun add(todo: Todo): ResultOf<Todo> {
        val errorMsg = "failed to add todo"
        val response = try {
            apolloClient.mutate(CreateTodoMutation(todo.name, todo.isDone)).await()
        } catch (e: ApolloException) {
            return ResultOf.Failure(
                message = errorMsg,
                throwable = DataSourceError.HttpNetworkError(e.message)
            )
        }

        response.data?.createTask?.let {
            return ResultOf.Success(Todo(it.id, it.name, it.isDone))

        }

        if (authError.equals(response.errors?.first()?.message, ignoreCase = false)) {
            return ResultOf.Failure(
                message = errorMsg,
                throwable = DataSourceError.AuthenticationError(authError)
            )
        }

        return ResultOf.Failure(
            message = errorMsg,
            throwable = DataSourceError.UnknownError(response.errors?.first()?.message)
        )
    }

    override suspend fun delete(id: String): ResultOf<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, isDone: Boolean): ResultOf<Todo> {
        TODO("Not yet implemented")
    }
}
