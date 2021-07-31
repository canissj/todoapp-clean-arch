package com.example.core.usecases

import com.example.core.data.AuthRepository

class GetToken(private val authRepository: AuthRepository) {
    operator fun invoke(): String? {
        return authRepository.getToken()
    }
}