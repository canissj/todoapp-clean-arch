package com.example.core.data

import com.example.core.domain.ResultOf

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun signIn(userName: String, key: String): ResultOf<String> {
        return authDataSource.signIn(userName, key)
    }
}
