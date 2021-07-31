package com.example.todoapp.todolist.framework.graphql

import CreateTokenMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.core.data.AuthDataSource
import com.example.core.domain.ResultOf

class GraphQLAuthDataSource(private val apolloClient: ApolloClient, private val apiKey: String) :
    AuthDataSource {
    override suspend fun signIn(userName: String): ResultOf<String> {
        val response = try {
            apolloClient.mutate(CreateTokenMutation(userName = userName, apiKey = apiKey)).await()
        } catch (e: ApolloException) {
            return ResultOf.Failure("failed to sign in", e)
        }

        return response.data?.generateAccessToken?.let {
            ResultOf.Success(it)
        } ?: run {
            ResultOf.Failure(message = "failed to sign in")
        }
    }
}
