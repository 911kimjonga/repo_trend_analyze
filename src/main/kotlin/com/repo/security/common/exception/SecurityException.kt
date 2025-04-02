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

    sealed class AccessTokenException(
        override val status: HttpStatus,
        override val message: String,
        override val cause: Throwable? = null
    ) : SecurityException(status, message, cause) {

        class InvalidAccessTokenException(
            override val message: String = "Invalid access token",
            override val cause: Throwable? = null,
        ): AccessTokenException(UNAUTHORIZED, message, cause)

        class ExpiredAccessTokenException(
            override val message: String = "Expired access token",
            override val cause: Throwable? = null,
        ): AccessTokenException(UNAUTHORIZED, message, cause)

        class InvalidRoleException(
            override val message: String = "Invalid roles",
            override val cause: Throwable? = null,
        ): AccessTokenException(FORBIDDEN, message, cause)

    }

    sealed class RefreshTokenException(
        override val status: HttpStatus,
        override val message: String,
        override val cause: Throwable? = null
    ) : SecurityException(status, message, cause) {

        class InvalidRefreshTokenException(
            override val message: String = "Invalid refresh token",
            override val cause: Throwable? = null,
        ): RefreshTokenException(UNAUTHORIZED, message, cause)

        class ExpiredRefreshTokenException(
            override val message: String = "Expired refresh token",
            override val cause: Throwable? = null,
        ): RefreshTokenException(UNAUTHORIZED, message, cause)

    }

}