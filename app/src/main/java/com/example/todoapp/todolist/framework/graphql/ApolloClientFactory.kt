package com.example.todoapp.todolist.framework.graphql

import com.apollographql.apollo.ApolloClient
import com.example.core.usecases.GetToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApolloClientFactory(private val getToken: GetToken) {

    private val log: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun getApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method, original.body)
                builder.header("Authorization", getToken() ?: "")
                chain.proceed(builder.build())
            }
            .addInterceptor(log)
            .build()

        return build(okHttpClient)
    }

    fun getApolloClientForAuth(): ApolloClient {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(log)
            .build()
        return build(okHttpClient)
    }

    private fun build(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient
            .builder()
            .serverUrl("https://380odjc5vi.execute-api.us-east-1.amazonaws.com/dev/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }
}