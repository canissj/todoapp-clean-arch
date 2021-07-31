package com.example.core.data

import com.example.core.domain.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @Mock
    private lateinit var authDataSource: AuthDataSource

    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(authDataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return credentials when success`() = runBlockingTest {
        // given
        val userName = "user"
        val key = "some key"
        val credentials = "credentials"
        `when`(authDataSource.signIn(userName, key)).thenReturn(ResultOf.Success(credentials))

        // when
        val result = authRepository.signIn(userName, key) as ResultOf.Success

        // then
        assertEquals(credentials, result.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return failure when error`() = runBlockingTest {
        // given
        val userName = "user"
        val key = "some key"
        `when`(authDataSource.signIn(userName, key)).thenReturn(ResultOf.Failure())

        // when
        val result = authRepository.signIn(userName, key)

        // then
        assertTrue(result is ResultOf.Failure)
    }
}