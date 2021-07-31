package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

class UpdateTodo(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(id: String, isDone: Boolean): ResultOf<Todo> {
        return todoRepository.updateTodo(id, isDone)
    }
}
