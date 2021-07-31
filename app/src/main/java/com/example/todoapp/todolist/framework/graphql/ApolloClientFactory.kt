package com.example.todoapp.todolist.framework.graphql

import com.apollographql.apollo.ApolloClient
import com.example.core.usecases.GetToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApolloClientFactory(private val getToken: GetToken) {
    fun getApolloClient(): ApolloClient {
        val log: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
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

        return ApolloClient
            .builder()
            .serverUrl("https://380odjc5vi.execute-api.us-east-1.amazonaws.com/dev/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }
}