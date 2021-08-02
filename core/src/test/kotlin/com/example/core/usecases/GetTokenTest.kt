package com.example.core.usecases

import com.example.core.data.AuthRepository
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
class GetTokenTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var getToken: GetToken

    @Before
    fun setUp() {
        getToken = GetToken(authRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getToken should return token`() = runBlockingTest {
        // given
        val credentials = "credentials"
        Mockito.`when`(authRepository.getToken()).thenReturn(credentials)

        // when
        val result = getToken()

        // then
        Assert.assertFalse(result.isNullOrEmpty())
        Assert.assertEquals(credentials, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getToken should return null token`() = runBlockingTest {
        // given
        Mockito.`when`(authRepository.getToken()).thenReturn(null)

        // when
        val result = getToken()

        // then
        Assert.assertTrue(result.isNullOrEmpty())
    }
}