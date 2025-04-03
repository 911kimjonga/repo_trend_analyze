package com.repo.auth.core.token.provider

import com.repo.auth.core.redis.enums.KeyType
import com.repo.auth.core.redis.service.RedisService
import com.repo.auth.domain.user.enums.UserRole
import com.repo.auth.domain.user.service.UserService
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class RefreshTokenProvider(
    private val redisService: RedisService,
    private val accessTokenProvider: AccessTokenProvider,
    private val userService: UserService,
) {

    fun generateRefreshToken(
        userId: String
    ): String {
        val refreshToken = UUID.randomUUID().toString()
        val expireSeconds = REFRESH_TOKEN_EXPIRE_SECONDS

        redisService.save(KeyType.REFRESH, refreshToken, userId, Duration.ofSeconds(expireSeconds))

        return refreshToken
    }

    fun reissueAccessToken(
        token: String
    ): String {
        val userId = redisService.get(KeyType.REFRESH, token)

        val user = userService.findUser(userId.toLong())
        return accessTokenProvider.generateAccessToken(
            user.id,
            UserRole.fromRole(user.userRole)
        )
    }

    fun rotateRefreshToken(
        oldToken: String
    ): String {
        val userId = redisService.get(KeyType.REFRESH, oldToken)

        val newToken = UUID.randomUUID().toString()
        redisService.save(KeyType.REFRESH, newToken, userId, Duration.ofDays(7))

        redisService.delete(KeyType.REFRESH, oldToken)

        return newToken
    }

    fun deleteRefreshToken(
        token: String
    ) {
        val userId = redisService.get(KeyType.REFRESH, token)
        redisService.delete(KeyType.REFRESH, token)
    }

    internal companion object {
        const val REFRESH_TOKEN_EXPIRE_SECONDS: Long = 7 * 24 * 60 * 60
    }

}