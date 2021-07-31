package com.example.core.usecases

import com.example.core.data.AuthRepository
import com.example.core.domain.ResultOf

class SignIn(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String): ResultOf<Boolean> {
        return authRepository.signIn(username)
    }
}