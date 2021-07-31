package com.example.todoapp.todolist.framework.injection

import com.example.core.data.TodoDataSource
import com.example.core.data.TodoRepository
import com.example.core.data.TodoRepositoryImpl
import com.example.core.data.TokenStorageDataSource
import com.example.core.usecases.GetAllTodos
import com.example.todoapp.todolist.framework.graphql.ApolloClientFactory
import com.example.todoapp.todolist.framework.graphql.GraphQLAuthDataSource
import com.example.todoapp.todolist.framework.graphql.GraphQLTodoDataSource
import com.example.todoapp.todolist.framework.storage.InMemoryTokenStorageDataSource
import com.example.todoapp.todolist.presentation.TodoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    factory { ApolloClientFactory(get()) }

    single {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClient()
    }

    factory(named("auth")) {
        val apolloClientFactory: ApolloClientFactory = get()
        apolloClientFactory.getApolloClient()
    }

    single<TokenStorageDataSource> { InMemoryTokenStorageDataSource() }

    factory<TodoDataSource> { GraphQLTodoDataSource(get()) }

    factory<TodoRepository> { TodoRepositoryImpl(get()) }

    factory { GetAllTodos(get()) }

    factory { GraphQLAuthDataSource(get(named("auth"))) }

    viewModel { TodoViewModel(get(), get()) }
}