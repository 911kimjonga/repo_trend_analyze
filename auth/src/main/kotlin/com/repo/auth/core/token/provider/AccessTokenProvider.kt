package com.repo.auth.core.token.provider

import com.repo.auth.common.exception.AuthException.*
import com.repo.auth.common.exception.AuthException.AccessTokenException.*
import com.repo.auth.core.redis.enums.AuthRedisKeyType.BLACKLIST
import com.repo.auth.core.token.config.JwtConfig
import com.repo.auth.core.redis.service.AuthRedisService
import com.repo.auth.core.token.constants.ACCESS_TOKEN_CLAIM_ROLE
import com.repo.auth.user.enums.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccessTokenProvider(
    private val config: JwtConfig,
    private val redisService: AuthRedisService,
) {

    private val key by lazy { Keys.hmacShaKeyFor(config.secret.toByteArray()) }

    fun generate(
        userId: String,
        userRole: UserRole,
    ): String {
        val now = Date()
        val expiry = Date(now.time + config.expiration)

        return Jwts.builder()
            .subject(userId)
            .claim(ACCESS_TOKEN_CLAIM_ROLE, userRole.role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun parseBearerToken(
        header: String
    ): String {
        return header.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.trim()
            ?: throw UnauthorizedException("Missing or malformed Authorization header")
    }

    fun validate(
        token: String,
        expectedRole: UserRole
    ): Boolean {
        val claims = this.getClaims(token)
        val role = UserRole.fromRole(claims[ACCESS_TOKEN_CLAIM_ROLE] as? String)

        when {
            redisService.has(
                BLACKLIST,
                token
            ) -> throw InvalidAccessTokenException("Invalid access token. Access token is blacklisted")

            role != expectedRole -> throw InvalidRoleException("Invalid roles. ExpectedRole is $expectedRole")
            else -> return true
        }
    }

    fun getUserId(
        token: String
    ): String {
        return this.getClaims(token).subject
    }

    fun addToBlackList(
        userId: String,
        token: String
    ) {
        redisService.save(BLACKLIST, token, userId, this.getRemainingTime(token))
    }

    private fun getRemainingTime(
        token: String
    ): Long {
        return maxOf(0, this.getClaims(token).expiration.time - Date().time)
    }

    private fun getClaims(
        token: String
    ): Claims {
        return runCatching {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        }.getOrElse {
            when (it) {
                is ExpiredJwtException -> throw ExpiredAccessTokenException()
                is UnsupportedJwtException -> throw InvalidAccessTokenException("Unsupported JWT format.")
                is MalformedJwtException -> throw InvalidAccessTokenException("Malformed JWT.")
                is SignatureException -> throw InvalidAccessTokenException("JWT signature does not match.")
                is IllegalArgumentException -> throw InvalidAccessTokenException("JWT is null or empty.")
                else -> throw InvalidAccessTokenException("Invalid access token. Failed getting claims")
            }
        }
    }

}