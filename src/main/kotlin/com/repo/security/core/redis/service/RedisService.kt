package com.repo.security.core.redis.service

import com.repo.security.common.exception.SecurityException.RefreshTokenException.InvalidRefreshTokenException
import com.repo.security.core.redis.enums.KeyType
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate
) {
    fun save(keyType: KeyType, token: String, userId: String, expiry: Duration) {
        redisTemplate.opsForValue().set("${keyType.type}:$token", userId, expiry)
    }

    fun delete(keyType: KeyType, token: String) {
        redisTemplate.delete("${keyType.type}:$token")
    }

    fun get(keyType: KeyType, token: String): String {
        return redisTemplate.opsForValue().get("${keyType.type}:$token") ?: throw InvalidRefreshTokenException()
    }

    fun has(keyType: KeyType, token: String): Boolean {
        return redisTemplate.hasKey("${keyType.type}:$token")
    }
}