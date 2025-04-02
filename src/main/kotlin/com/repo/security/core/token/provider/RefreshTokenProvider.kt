package com.repo.security.core.token.provider

import com.repo.security.common.exception.SecurityException.RefreshTokenException.*
import com.repo.security.core.token.model.AccessTokenRequestDto
import com.repo.security.core.redis.service.RedisService
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.service.UserService
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class RefreshTokenProvider(
    private val redisService: RedisService,
    private val accessTokenProvider: AccessTokenProvider,
    private val userService: UserService,
) {
    fun generateRefreshToken(userId: String): String {
        val refreshToken = UUID.randomUUID().toString()
        val expireSeconds = REFRESH_TOKEN_EXPIRE_SECONDS

        redisService.save(refreshToken, userId, Duration.ofSeconds(expireSeconds))

        return refreshToken
    }

    fun reissueAccessToken(token: String): String {
        val userId = redisService.get(token)

        // 토큰 유효 → Access Token 발급
        val user = userService.findUser(userId.toLong())
        return accessTokenProvider.generateAccessToken(
            AccessTokenRequestDto(
                id = user.id,
                role = UserRole.fromRole(user.userRole)
            )
        )
    }

    fun rotateRefreshToken(oldToken: String): String {
        val userId = redisService.get(oldToken)

        val newToken = UUID.randomUUID().toString()
        redisService.save(newToken, userId, Duration.ofDays(7))

        redisService.delete(oldToken)

        return newToken
    }

    internal companion object {
        const val REFRESH_TOKEN_EXPIRE_SECONDS: Long = 7 * 24 * 60 * 60
    }
}