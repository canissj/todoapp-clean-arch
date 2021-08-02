package com.example.todoapp.todolist.framework.storage

import com.example.todoapp.todolist.framework.storage.InMemoryTokenStorageDataSource
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryTokenStorageDataSourceTest {

    private lateinit var inMemoryTokenStorageDataSource: InMemoryTokenStorageDataSource

    @Before
    fun setUp() {
        inMemoryTokenStorageDataSource = InMemoryTokenStorageDataSource()
    }

    @Test
    fun `getToken should return null when no token is available`() {
        // given

        // when
        val token = inMemoryTokenStorageDataSource.getToken()

        // then
        assertNull(token)
    }

    @Test
    fun `getToken should return token`() {
        // given
        val expectedToken = "some_token"
        inMemoryTokenStorageDataSource.saveToken(expectedToken)

        // when
        val token = inMemoryTokenStorageDataSource.getToken()

        // then
        assertEquals(expectedToken, token)
    }
}