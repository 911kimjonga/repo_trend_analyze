package com.repo.security.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

sealed class SecurityException(
    open val status: HttpStatus,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    class UnauthenticatedException(
        override val message: String = "Unauthenticated",
        override val cause: Throwable? = null,
    ) : SecurityException(UNAUTHORIZED, message, cause)

    class UnauthorizedException(
        override val message: String = "Unauthorized",
        override val cause: Throwable? = null,
    ) : SecurityException(FORBIDDEN, message, cause)

    class InvalidRoleException(
        override val message: String = "Invalid roles",
        override val cause: Throwable? = null,
    ): JwtException(UNAUTHORIZED, message, cause)

    sealed class JwtException(
        override val status: HttpStatus,
        override val message: String,
        override val cause: Throwable? = null
    ) : SecurityException(status, message, cause) {

        class InvalidTokenException(
            override val message: String = "Invalid token",
            override val cause: Throwable? = null,
        ): JwtException(UNAUTHORIZED, message, cause)

        class ExpiredTokenException(
            override val message: String = "Expired token",
            override val cause: Throwable? = null,
        ): JwtException(UNAUTHORIZED, message, cause)

    }

}