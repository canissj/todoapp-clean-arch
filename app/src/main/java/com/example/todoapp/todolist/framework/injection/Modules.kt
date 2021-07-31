package com.example.todoapp.todolist.framework.injection

import com.apollographql.apollo.ApolloClient
import com.example.core.data.TodoDataSource
import com.example.core.data.TodoRepository
import com.example.core.data.TodoRepositoryImpl
import com.example.todoapp.todolist.framework.graphql.GraphQLTodoDataSource
import com.example.todoapp.todolist.presentation.TodoViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    factory <ApolloClient> {
        val accessToken = ""
        val log: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method, original.body)
                builder.header("Authorization", accessToken)
                chain.proceed(builder.build())
            }
            .addInterceptor(log)
            .build()

        ApolloClient
            .builder()
            .serverUrl("https://380odjc5vi.execute-api.us-east-1.amazonaws.com/dev/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }

    factory<TodoDataSource> { GraphQLTodoDataSource(get()) }

    factory<TodoRepository> { TodoRepositoryImpl(get()) }

    viewModel { TodoViewModel(get()) }
}