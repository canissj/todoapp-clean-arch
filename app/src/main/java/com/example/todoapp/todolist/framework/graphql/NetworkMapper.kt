package com.example.todoapp.todolist.framework.graphql

import com.example.core.domain.Todo

object NetworkMapper {
    fun toListOfTodos(queryTodos: List<GetAllTasksQuery.AllTask?>?): List<Todo> {
        return queryTodos?.let {
            it.map { task ->
                // we assume that task list does not hold null values. it should hold tasks
                Todo(
                    id = task!!.id,
                    name = task.name,
                    note = task.note,
                    isDone = task.isDone
                )
            }
        } ?: listOf()
    }
}