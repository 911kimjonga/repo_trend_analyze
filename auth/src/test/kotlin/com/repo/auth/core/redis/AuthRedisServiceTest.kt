package com.repo.auth.core.redis

import com.repo.auth.core.redis.enums.AuthRedisKeyType
import com.repo.auth.core.redis.service.AuthRedisService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class AuthRedisServiceTest {

    @Autowired
    lateinit var redisService: AuthRedisService

    companion object {
        private lateinit var keyType: AuthRedisKeyType
        private lateinit var token: String
        private lateinit var userId: String
        private var ttl: Long = 0
    }

    @BeforeEach
    fun setup() {
        keyType = AuthRedisKeyType.REFRESH
        token = "test-token-123"
        userId = "user123"
        ttl = Duration.ofMinutes(5).toSeconds()
    }

    @Test
    fun test() {

        redisService.save(keyType, token, userId, ttl)

        val has = redisService.has(keyType, token)
        val get = redisService.get(keyType, token)

        assertTrue(has)
        assertEquals(userId, get)

        redisService.delete(keyType, token)

        val hasNot = redisService.has(keyType, token)

        assertFalse(hasNot)
        assertFails { redisService.get(keyType, token) }

    }

}