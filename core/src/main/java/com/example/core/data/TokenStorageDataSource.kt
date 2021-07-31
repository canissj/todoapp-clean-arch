package com.example.core.data

interface TokenStorageDataSource {
    fun saveToken(token: String)
    fun getToken(): String?
}