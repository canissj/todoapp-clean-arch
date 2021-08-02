package com.example.todoapp.todolist.framework

sealed class DataSourceError : Throwable() {
    data class AuthenticationError(val msg: String?) : DataSourceError()
    data class HttpNetworkError(val msg: String?) : DataSourceError()
    data class UnknownError(val msg: String?) : DataSourceError()
}