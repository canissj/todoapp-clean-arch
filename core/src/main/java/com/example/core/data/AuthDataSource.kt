package com.example.core.data

import com.example.core.domain.ResultOf

interface AuthDataSource {
    suspend fun signIn(userName: String): ResultOf<String>
}