package com.example.core.data

import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

interface TodoDataSource {
    suspend fun getAll(): ResultOf<List<Todo>>

    suspend fun add(todo: Todo): ResultOf<Todo>

    suspend fun delete(id: String): ResultOf<Boolean>

    suspend fun update(id: String, isDone: Boolean): ResultOf<Todo>
}
