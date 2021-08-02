package com.example.todoapp.todolist.framework.injection

import com.example.core.data.AuthDataSource
import com.example.core.data.AuthRepository
import com.example.core.data.AuthRepositoryImpl
import com.example.core.data.TodoDataSource
import com.example.core.data.TodoRepository
import com.example.core.data.TodoRepositoryImpl
import com.example.core.data.TokenStorageDataSource
import com.example.core.usecases.AddTodo
import com.example.core.usecases.GetAllTodos
import com.example.core.usecases.GetToken
import com.example.core.usecases.SignIn
import com.example.todoapp.BuildConfig
import com.example.todoapp.todolist.framework.graphql.ApolloClientFactory
import com.example.todoapp.todolist.framework.graphql.GraphQLAuthDataSource
import com.example.todoapp.todolist.framework.graphql.GraphQLTodoDataSource
import com.example.todoapp.todolist.framework.storage.InMemoryTokenStorageDataSource
import com.example.todoapp.todolist.presentation.TodoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    // apollo
    factory { ApolloClientFactory(tokenStorageDataSource = get()) }

    single {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClient()
    }

    factory(named("auth")) {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClientForAuth()
    }

    // data sources
    factory<AuthDataSource> {
        GraphQLAuthDataSource(
            apolloClient = get(named("auth")),
            apiKey = BuildConfig.API_KEY
        )
    }

    factory<TodoDataSource> { GraphQLTodoDataSource(apolloClient = get()) }

    single<TokenStorageDataSource> { InMemoryTokenStorageDataSource() }

    // repositories
    single<TodoRepository> { TodoRepositoryImpl(todoDataSource = get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authDataSource = get(),
            tokenStorageDataSource = get()
        )
    }

    // use cases
    factory { GetToken(authRepository = get()) }

    factory { SignIn(authRepository = get()) }

    factory { GetAllTodos(todoRepository = get()) }

    factory { AddTodo(todoRepository = get()) }

    // view models
    viewModel {
        TodoViewModel(
            signIn = get(),
            getAllTodos = get(),
            addTodo = get(),
            getToken = get()
        )
    }
}
