package com.example.core.data

import com.example.core.domain.ResultOf

interface AuthRepository {
    suspend fun signIn(userName: String): ResultOf<Boolean>
    fun getToken(): String?
}