package com.example.core.data

import com.example.core.domain.ResultOf

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val tokenStorageDataSource: TokenStorageDataSource
) : AuthRepository {

    override suspend fun signIn(userName: String, key: String): ResultOf<Boolean> {
        return when (val signInResult = authDataSource.signIn(userName, key)) {
            is ResultOf.Success -> {
                tokenStorageDataSource.saveToken(signInResult.value)
                ResultOf.Success(true)
            }
            is ResultOf.Failure -> {
                ResultOf.Success(false)
            }
        }
    }

    override fun getToken(): String? {
        return tokenStorageDataSource.getToken()
    }
}
