package com.repo.auth.core.redis

import com.repo.auth.core.redis.enums.AuthRedisKeyType
import com.repo.auth.core.redis.service.AuthRedisService
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
class RedisServiceTest {

    @Autowired
    lateinit var redisService: AuthRedisService

    @Test
    fun test() {
        val keyType = AuthRedisKeyType.REFRESH
        val token = "test-token-123"
        val userId = "user123"
        val expiry = Duration.ofMinutes(5)

        redisService.save(keyType, token, userId, expiry)

        val has = redisService.has(keyType, token)
        val get = redisService.get(keyType, token)

        assertTrue(has)
        assertEquals(userId, get)

        redisService.delete(keyType, token)

        val hasNot = redisService.has(keyType, token)

        assertFalse(hasNot)
        assertFails {
            redisService.get(keyType, token)
        }
    }

}