package com.example.core.data

import com.example.core.domain.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @Mock
    private lateinit var authDataSource: AuthDataSource

    @Mock
    private lateinit var tokenDataSource: TokenStorageDataSource

    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(authDataSource, tokenDataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return credentials when success`() = runBlockingTest {
        // given
        val userName = "user"
        val credentials = "credentials"
        `when`(authDataSource.signIn(userName)).thenReturn(ResultOf.Success(credentials))

        // when
        val result = authRepository.signIn(userName) as ResultOf.Success

        // then
        assertTrue(result.value)
        verify(tokenDataSource).saveToken(credentials)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return failure when error`() = runBlockingTest {
        // given
        val userName = "user"
        `when`(authDataSource.signIn(userName)).thenReturn(ResultOf.Failure())

        // when
        val result = authRepository.signIn(userName)

        // then
        assertTrue(result is ResultOf.Failure)
        verifyNoInteractions(tokenDataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getToken should return token`() = runBlockingTest {
        // given
        val credentials = "credentials"
        `when`(tokenDataSource.getToken()).thenReturn(credentials)

        // when
        val result = authRepository.getToken()

        // then
        assertFalse(result.isNullOrEmpty())
        assertEquals(credentials, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getToken should return null token`() = runBlockingTest {
        // given
        `when`(tokenDataSource.getToken()).thenReturn(null)

        // when
        val result = authRepository.getToken()

        // then
        assertTrue(result.isNullOrEmpty())
    }
}