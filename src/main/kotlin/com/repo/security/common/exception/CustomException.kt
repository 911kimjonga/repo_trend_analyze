package com.repo.security.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

sealed class CustomException(
    val status: HttpStatus,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    class UnauthenticatedException(
        override val message: String = "Unauthenticated",
        override val cause: Throwable? = null,
    ) : CustomException(UNAUTHORIZED, message, cause)

    class UnauthorizedException(
        override val message: String = "Unauthorized",
        override val cause: Throwable? = null,
    ) : CustomException(FORBIDDEN, message, cause)

}