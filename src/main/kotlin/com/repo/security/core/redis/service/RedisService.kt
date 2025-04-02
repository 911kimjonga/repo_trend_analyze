package com.repo.security.core.redis.service

import com.repo.security.common.exception.SecurityException.RefreshTokenException.InvalidRefreshTokenException
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate
) {
    fun save(token: String, userId: String, expiry: Duration) {
        redisTemplate.opsForValue().set("refresh:$token", userId, expiry)
    }

    fun delete(token: String) {
        redisTemplate.delete("refresh:$token")
    }

    fun getUserId(token: String): String {
        return redisTemplate.opsForValue().get("refresh:$token") ?: throw InvalidRefreshTokenException()
    }
}