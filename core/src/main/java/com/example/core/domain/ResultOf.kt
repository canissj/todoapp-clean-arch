package com.example.core.domain

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Failure(
        val message: String? = null,
        val throwable: Throwable? = null,
    ) : ResultOf<Nothing>()
}