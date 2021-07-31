package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

class DeleteTodo(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(id: String): ResultOf<Boolean> {
        return todoRepository.deleteTodo(id)
    }
}