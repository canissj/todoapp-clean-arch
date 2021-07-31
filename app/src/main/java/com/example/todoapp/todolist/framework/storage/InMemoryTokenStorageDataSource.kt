package com.example.todoapp.todolist.framework.storage

import com.example.core.data.TokenStorageDataSource
import java.util.concurrent.atomic.AtomicReference

class InMemoryTokenStorageDataSource : TokenStorageDataSource {

    var tokenCache: AtomicReference<String?> = AtomicReference()

    override fun saveToken(token: String) {
        tokenCache.set(token)
    }

    override fun getToken(): String? {
        return tokenCache.get()
    }
}