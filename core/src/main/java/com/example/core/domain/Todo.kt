package com.example.core.domain

data class Todo(
    val id: String = "",
    val name: String,
    val isDone: Boolean = false
)