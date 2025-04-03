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

    fun save(keyType: KeyType, token: String, userId: String, expiry: Duration) =
        redisTemplate.opsForValue().set("${keyType.type}:$token", userId, expiry)

    fun delete(keyType: KeyType, token: String) =
        redisTemplate.delete("${keyType.type}:$token")

    fun get(keyType: KeyType, token: String): String =
        redisTemplate.opsForValue().get("${keyType.type}:$token")
            ?: throw InvalidRefreshTokenException()

    fun has(keyType: KeyType, token: String): Boolean =
        redisTemplate.hasKey("${keyType.type}:$token")

}