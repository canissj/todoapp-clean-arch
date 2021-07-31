package com.example.core.domain

data class Todo(
    val id: String = "",
    val name: String,
    val note: String? = null,
    val isDone: Boolean
)