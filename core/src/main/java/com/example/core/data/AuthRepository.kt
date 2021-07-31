package com.example.core.data

import com.example.core.domain.ResultOf

interface AuthRepository {
    suspend fun signIn(userName: String, key: String): ResultOf<String>
}