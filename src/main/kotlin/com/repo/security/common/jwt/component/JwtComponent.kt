package com.repo.security.common.jwt.component

import com.repo.security.common.jwt.config.JwtConfig
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtComponent(
    private val config: JwtConfig,
) {
    private val key by lazy { Keys.hmacShaKeyFor(config.secret.toByteArray()) }

    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + config.expiration)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return runCatching {
            Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
            true
        }.getOrElse { false }
    }

    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

}