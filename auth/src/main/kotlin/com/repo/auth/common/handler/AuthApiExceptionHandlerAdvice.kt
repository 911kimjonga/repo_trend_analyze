package com.repo.auth.common.handler

import com.repo.auth.common.exception.AuthException
import com.repo.common.response.ApiResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthApiExceptionHandlerAdvice {

    @ExceptionHandler(
        AuthException::class
    )
    fun handleAuthException(
        ex: AuthException
    ): ApiResponse<Unit> =
        ApiResponse.error(
            code = ex.status.value().toString(),
            message = ex.message
        )

}