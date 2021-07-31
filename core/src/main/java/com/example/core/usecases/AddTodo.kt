package com.example.core.usecases

import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo

class AddTodo(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Todo): ResultOf<Todo> {
        return todoRepository.addTodo(todo)
    }
}