package com.repo.auth.core.redis.service

import com.repo.auth.common.exception.AuthException.RefreshTokenException.InvalidRefreshTokenException
import com.repo.auth.core.redis.enums.KeyType
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate
) {
    fun save(keyType: KeyType, key: String, value: String, expiry: Duration) {
        redisTemplate.opsForValue().set("${keyType.type}:$key", value, expiry)
    }

    fun delete(keyType: KeyType, key: String) {
        redisTemplate.delete("${keyType.type}:$key")
    }

    fun get(keyType: KeyType, key: String): String {
        return redisTemplate.opsForValue().get("${keyType.type}:$key") ?: throw InvalidRefreshTokenException()
    }

    fun has(keyType: KeyType, key: String): Boolean {
        return redisTemplate.hasKey("${keyType.type}:$key")
    }
}