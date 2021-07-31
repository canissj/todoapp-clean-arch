package com.example.core.data

import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

class TodoRepositoryImpl(private val todoDataSource: TodoDataSource) : TodoRepository {

    override suspend fun getAllTodos(): ResultOf<List<Todo>> {
        return todoDataSource.getAll()
    }

    override suspend fun addTodo(todo: Todo): ResultOf<Todo> {
        return todoDataSource.add(todo)
    }

    override suspend fun deleteTodo(id: String): ResultOf<Boolean> {
        return todoDataSource.delete(id)
    }

    override suspend fun updateTodo(id: String, isDone: Boolean): ResultOf<Todo> {
        return todoDataSource.update(id, isDone)
    }
}
