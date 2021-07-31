package com.example.todoapp.todolist.framework.graphql

import GetAllTasksQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.example.core.data.TodoDataSource
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.todoapp.todolist.framework.DataSourceError

class GraphQLTodoDataSource(private val apolloClient: ApolloClient) : TodoDataSource {

    override suspend fun getAll(): ResultOf<List<Todo>> {
        val response = try {
            apolloClient
                .query(GetAllTasksQuery())
                .await()
        } catch (e: ApolloNetworkException) {
            return ResultOf.Failure(
                "fail to fetch todos",
                throwable = DataSourceError.HttpConnectivityNetworkError(e.message)
            )
        } catch (e: ApolloException) {
            return ResultOf.Failure(
                "fail to fetch todos",
                throwable = DataSourceError.HttpNetworkError(e.message)
            )
        }

        response.data?.let {
            val todos = NetworkMapper.toListOfTodos(it.allTasks)
            return ResultOf.Success(todos)
        }

        return ResultOf.Failure(
            message = "failed to fetch todos",
            throwable = DataSourceError.UnknownError(response.errors?.first()?.message)
        )
    }

    override suspend fun add(todo: Todo): ResultOf<Todo> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): ResultOf<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, isDone: Boolean): ResultOf<Todo> {
        TODO("Not yet implemented")
    }
}
