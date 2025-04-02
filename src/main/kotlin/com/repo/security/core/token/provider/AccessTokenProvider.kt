package com.repo.security.core.token.provider

import com.repo.security.common.exception.SecurityException.*
import com.repo.security.common.exception.SecurityException.AccessTokenException.*
import com.repo.security.core.config.JwtConfig
import com.repo.security.core.token.enums.AccessTokenClaims.*
import com.repo.security.domain.user.enums.UserRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccessTokenProvider(
    private val config: JwtConfig,
) {
    private val key by lazy { Keys.hmacShaKeyFor(config.secret.toByteArray()) }

    fun generateAccessToken(
        userId: String,
        userRole: UserRole,
    ): String {
        val now = Date()
        val expiry = Date(now.time + config.expiration)

        return Jwts.builder()
            .subject(userId)
            .claim(ROLE.claim, userRole.role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun extractToken(
        header: String
    ): String {
        if (!header.startsWith("Bearer ")) {
            throw UnauthorizedException()
        }
        return header.removePrefix("Bearer ").trim()
    }

    fun validateToken(
        token: String,
        expectedRole: UserRole
    ): Boolean {
        val claims = this.getClaims(token)
        val role = UserRole.fromRole(claims[ROLE.claim] as? String)

        when {
            claims.expiration.before(Date()) -> throw ExpiredAccessTokenException()
            role != expectedRole -> throw InvalidRoleException()
            else -> return true
        }
    }

    fun getIdByToken(
        token: String
    ): String =
        this.getClaims(token).subject

    private fun getClaims(token: String) =
        runCatching {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        }.getOrElse {
            throw InvalidAccessTokenException()
        }

}