package com.example.todoapp.todolist.framework.injection

import com.example.core.data.*
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

    single<TokenStorageDataSource> { InMemoryTokenStorageDataSource() }

    factory { ApolloClientFactory(tokenStorageDataSource = get()) }

    single {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClient()
    }

    factory(named("auth")) {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClientForAuth()
    }

    factory<TodoDataSource> { GraphQLTodoDataSource(apolloClient = get()) }

    single<TodoRepository> { TodoRepositoryImpl(todoDataSource = get()) }

    factory { GetAllTodos(todoRepository = get()) }

    // auth
    single<AuthRepository> {
        AuthRepositoryImpl(
            authDataSource = get(),
            tokenStorageDataSource = get()
        )
    }

    factory<AuthDataSource> {
        GraphQLAuthDataSource(
            apolloClient = get(named("auth")),
            apiKey = BuildConfig.API_KEY
        )
    }

    factory { GetToken(authRepository = get()) }

    factory { SignIn(authRepository = get()) }

    // view models
    viewModel { TodoViewModel(signIn = get(), getAllTodos = get(), getToken = get()) }
}
