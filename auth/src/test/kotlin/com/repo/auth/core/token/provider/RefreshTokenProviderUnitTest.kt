package com.repo.auth.core.token.provider

import com.repo.auth.core.redis.enums.AuthRedisKeyType
import com.repo.auth.core.redis.enums.AuthRedisKeyType.REFRESH
import com.repo.auth.core.redis.service.AuthRedisService
import com.repo.auth.core.token.constants.REFRESH_TOKEN_TTL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class RefreshTokenProviderUnitTest {

    @Mock
    private lateinit var redisService: AuthRedisService

    @InjectMocks
    lateinit var refreshTokenProvider: RefreshTokenProvider

    companion object {
        private lateinit var userId: String
        private lateinit var token: String
        private lateinit var tokenType: AuthRedisKeyType
        private var ttl: Long = 0L
    }

    @BeforeEach
    fun setUp() {
        userId = "1"
        token = UUID.randomUUID().toString()
        tokenType = REFRESH
        ttl = REFRESH_TOKEN_TTL
    }

    @Test
    fun generate() {

        val userId = userId

        whenever(redisService.save(eq(tokenType), any(), eq(userId), eq(ttl))).then {  }

        val result = refreshTokenProvider.generate(userId)

        verify(redisService).save(eq(tokenType), any(), eq(userId), eq(ttl))

    }

    @Test
    fun delete() {

        val token = token
        val userId = userId

        whenever(redisService.get(eq(tokenType), any())).thenReturn(userId)
        whenever(redisService.delete(eq(tokenType), any())).thenReturn(true)

        val result = refreshTokenProvider.delete(token)

        assertEquals(userId, result)
        verify(redisService).get(eq(tokenType), any())
        verify(redisService).delete(eq(tokenType), any())

    }

    @Test
    fun rotate() {

        val token = token
        val userId = userId
        val ttl = ttl

        whenever(redisService.get(eq(tokenType), any())).thenReturn(userId)
        whenever(redisService.save(eq(tokenType), any(), eq(userId), eq(ttl))).then { }
        whenever(redisService.delete(eq(tokenType), any())).thenReturn(true)

        val result = refreshTokenProvider.rotate(token)

        verify(redisService).get(eq(tokenType), any())
        verify(redisService).save(eq(tokenType), any(), eq(userId), eq(ttl))
        verify(redisService).delete(eq(tokenType), any())

    }


}