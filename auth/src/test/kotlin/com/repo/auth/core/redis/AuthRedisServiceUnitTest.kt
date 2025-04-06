package com.repo.auth.core.redis

import com.repo.auth.core.redis.enums.AuthRedisKeyType
import com.repo.auth.core.redis.service.AuthRedisService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class AuthRedisServiceUnitTest {

    @Mock
    lateinit var redisTemplate: StringRedisTemplate

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>

    @InjectMocks
    lateinit var authRedisService: AuthRedisService

    companion object {
        private lateinit var keyType: AuthRedisKeyType
        private lateinit var token: String
        private lateinit var key: String
        private lateinit var userId: String
        private var ttl: Long = 0
    }

    @BeforeEach
    fun setup() {
        keyType = AuthRedisKeyType.REFRESH
        token = "token123"
        key = "${keyType.type}:$token"
        userId = "user123"
        ttl = Duration.ofMinutes(30).toSeconds()

        lenient().whenever(redisTemplate.opsForValue()).thenReturn(valueOperations)
    }

    @Test
    fun save() {

        val keyType = keyType
        val token = token
        val key = key
        val userId = userId
        val ttl = ttl

        whenever(valueOperations.set(key, userId, ttl, TimeUnit.SECONDS)).then {  }

        val result = authRedisService.save(keyType, token, userId, ttl)

        verify(valueOperations).set(key, userId, ttl, TimeUnit.SECONDS)

    }

    @Test
    fun delete() {

        val keyType = keyType
        val token = token
        val key = key

        whenever(redisTemplate.delete(key)).thenReturn(true)

        val result = authRedisService.delete(keyType, token)

        assertTrue(result)
        verify(redisTemplate).delete(key)

    }

    @Test
    fun get() {

        val keyType = keyType
        val token = token
        val key = key
        val userId = userId

        whenever(valueOperations.get(key)).thenReturn(userId)

        val result = authRedisService.get(keyType, token)

        assertEquals(userId, result)
        verify(valueOperations).get(key)
    }

    @Test
    fun has() {

        val keyType = keyType
        val token = token
        val key = key

        whenever(redisTemplate.hasKey(key)).thenReturn(true)

        val result = authRedisService.has(keyType, token)

        assertTrue(result)
        verify(redisTemplate).hasKey(key)

    }

    @Test
    fun hasNot() {

        val keyType = keyType
        val token = token
        val key = key

        whenever(redisTemplate.hasKey(key)).thenReturn(false)

        val result = authRedisService.has(keyType, token)

        assertFalse(result)
        verify(redisTemplate).hasKey(key)

    }

}