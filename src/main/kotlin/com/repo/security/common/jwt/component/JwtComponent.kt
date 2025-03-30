package com.repo.security.common.jwt.component

import com.repo.security.common.jwt.config.JwtConfig
import com.repo.security.user.enums.UserRole
import com.repo.security.user.model.dto.response.SignInResponseDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtComponent(
    private val config: JwtConfig,
) {
    private val key by lazy { Keys.hmacShaKeyFor(config.secret.toByteArray()) }

    fun generateToken(user: SignInResponseDto): String {
        val now = Date()
        val expiry = Date(now.time + config.expiration)

        return Jwts.builder()
            .subject(user.id)
            .claim("role", user.userRole)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String, expectedRole: UserRole): Boolean {
        return runCatching {
            val claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .payload

            val isExpired = claims.expiration.before(Date())
            if (isExpired) return false

            val roleInToken = claims["role"] as? String
            if (roleInToken == null || roleInToken != expectedRole.role) return false

            true
        }.getOrElse { false }
    }

    fun getIdFromToken(token: String): String {
        val claims = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

}