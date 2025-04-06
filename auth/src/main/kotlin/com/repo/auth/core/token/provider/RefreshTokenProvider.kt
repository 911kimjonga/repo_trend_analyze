package com.repo.auth.core.token.provider

import com.repo.auth.core.redis.enums.AuthRedisKeyType.REFRESH
import com.repo.auth.core.redis.service.AuthRedisService
import com.repo.auth.core.token.constants.REFRESH_TOKEN_TTL
import org.springframework.stereotype.Component
import java.util.*

@Component
class RefreshTokenProvider(
    private val redisService: AuthRedisService,
) {

    fun generate(
        userId: String
    ): String {
        val refreshToken = UUID.randomUUID().toString()
        val ttl = REFRESH_TOKEN_TTL

        redisService.save(REFRESH, refreshToken, userId, ttl)

        return refreshToken
    }

    fun delete(
        token: String
    ): String {
        val userId = this.getUserId(token)

        redisService.delete(REFRESH, token)

        return userId
    }

    fun rotate(
        oldToken: String
    ): String {
        val userId = this.getUserId(oldToken)

        val newToken = this.generate(userId)

        redisService.delete(REFRESH, oldToken)

        return newToken
    }

    fun getUserId(
        token: String
    ): String =
        redisService.get(REFRESH, token)

}