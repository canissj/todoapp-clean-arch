package com.example.core.data

import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

interface TodoRepository {
    suspend fun getAllTodos(): ResultOf<List<Todo>>

    suspend fun addTodo(todo: Todo): ResultOf<Todo>

    suspend fun deleteTodo(id: String): ResultOf<Boolean>

    suspend fun updateTodo(id: String, isDone: Boolean): ResultOf<Todo>
}