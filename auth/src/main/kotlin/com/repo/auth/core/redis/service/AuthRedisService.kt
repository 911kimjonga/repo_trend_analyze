package com.repo.auth.core.redis.service

import com.repo.auth.common.exception.AuthException.RefreshTokenException.InvalidRefreshTokenException
import com.repo.auth.core.redis.enums.AuthRedisKeyType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AuthRedisService(
    @Qualifier("authRedisTemplate") private val redisTemplate: StringRedisTemplate
) {

    fun save(
        keyType: AuthRedisKeyType,
        token: String,
        userId: String,
        ttl: Long
    ) {
        redisTemplate.opsForValue().set(
            "${keyType.type}:$token",
            userId,
            ttl,
            TimeUnit.SECONDS
        )
    }

    fun delete(
        keyType: AuthRedisKeyType,
        token: String
    ) {
        redisTemplate.delete(
            "${keyType.type}:$token"
        )
    }

    fun get(
        keyType: AuthRedisKeyType,
        token: String
    ): String {
        return redisTemplate.opsForValue().get("${keyType.type}:$token")
            ?: throw InvalidRefreshTokenException()
    }

    fun has(
        keyType: AuthRedisKeyType,
        token: String
    ): Boolean {
        return redisTemplate.hasKey(
            "${keyType.type}:$token"
        )
    }

}