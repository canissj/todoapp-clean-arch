package com.example.todoapp.todolist.framework

import CreateTokenMutation
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.api.Response
import com.example.core.data.AuthDataSource
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.todoapp.todolist.framework.graphql.GraphQLAuthDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class GraphQLAuthDataSourceTest {
    @Mock
    private lateinit var apolloClient: ApolloClient

    @Mock
    private lateinit var mutationCall: ApolloMutationCall<CreateTokenMutation.Data>

    @Captor
    private lateinit var mutationCaptor: ArgumentCaptor<ApolloCall.Callback<CreateTokenMutation.Data>>

    private lateinit var graphQlAuthDataSource: AuthDataSource

    private val apiKey: String = "some_key"

    @Before
    fun setUp() {
        graphQlAuthDataSource = GraphQLAuthDataSource(apolloClient, apiKey)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `sigIn should return credential when success`() = runBlockingTest {
        // given
        val accessToken = "access_token"

        val data = CreateTokenMutation.Data(generateAccessToken = accessToken)
        val builder =
            Response.builder<CreateTokenMutation.Data>(CreateTokenMutation("", "")).data(data)
        val response = Response(builder)

        Mockito.`when`(apolloClient.mutate(any(CreateTokenMutation::class.java)))
            .thenReturn(mutationCall)

        Mockito.`when`(mutationCall.enqueue(mutationCaptor.capture())).thenAnswer {
            mutationCaptor.value.onResponse(response)
        }

        // when
        val resultSignIn = graphQlAuthDataSource.signIn("username") as ResultOf.Success

        // then
        assertEquals(accessToken, resultSignIn.value)
    }
}