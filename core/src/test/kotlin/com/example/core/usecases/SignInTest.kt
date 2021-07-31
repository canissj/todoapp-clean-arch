package com.example.core.usecases

import com.example.core.data.AuthRepository
import com.example.core.domain.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignInTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var signIn: SignIn

    @Before
    fun setUp() {
        signIn = SignIn(authRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return credentials when success`() = runBlockingTest {
        // given
        val userName = "user"
        val credentials = "credentials"
        Mockito.`when`(signIn(userName)).thenReturn(ResultOf.Success(true))

        // when
        val result = authRepository.signIn(userName) as ResultOf.Success

        // then
        Assert.assertEquals(credentials, result.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signIn should return failure when error`() = runBlockingTest {
        // given
        val userName = "user"
        val key = "some key"
        Mockito.`when`(signIn(userName)).thenReturn(ResultOf.Failure())

        // when
        val result = authRepository.signIn(userName)

        // then
        Assert.assertTrue(result is ResultOf.Failure)
    }
}
