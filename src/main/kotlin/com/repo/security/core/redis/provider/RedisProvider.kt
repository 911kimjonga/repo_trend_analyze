package com.repo.security.core.redis.provider

import com.repo.security.core.jwt.model.JwtRequestDto
import com.repo.security.core.jwt.provider.JwtProvider
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.service.UserService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class RedisProvider(
    private val redisTemplate: StringRedisTemplate,
    private val jwtProvider: JwtProvider,
    private val userService: UserService,
) {
    fun generateRefreshToken(userId: String): String {
        val refreshToken = UUID.randomUUID().toString()
        val expireSeconds = REFRESH_TOKEN_EXPIRE_SECONDS

        redisTemplate.opsForValue().set("refresh:$userId", refreshToken, Duration.ofSeconds(expireSeconds))

        return refreshToken
    }

    fun reissueAccessToken(userId: String, providedRefreshToken: String): String? {
        val storedToken = redisTemplate.opsForValue().get("refresh:$userId") ?: return null

        if (storedToken != providedRefreshToken) {
            return null
        }

        // 토큰 유효 → Access Token 발급
        val user = userService.findUser(userId.toLong())
        return jwtProvider.generateAccessToken(
            JwtRequestDto(
                id = user.id,
                role = UserRole.fromRole(user.userRole)
            )
        )
    }

    fun rotateRefreshToken(userId: String, oldToken: String): String? {
        val storedToken = redisTemplate.opsForValue().get("refresh:$userId") ?: return null
        if (storedToken != oldToken) return null

        val newToken = UUID.randomUUID().toString()
        redisTemplate.opsForValue().set("refresh:$userId", newToken, Duration.ofDays(7))

        return newToken
    }

    internal companion object {
        const val REFRESH_TOKEN_EXPIRE_SECONDS: Long = 7 * 24 * 60 * 60
    }
}