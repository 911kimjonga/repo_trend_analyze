package com.repo.auth.common.exception

import com.repo.common.response.ApiResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthApiExceptionHandlerAdvice {

    @ExceptionHandler(
        AuthException::class
    )
    fun handleSecurityException(
        ex: AuthException
    ): ApiResponse<Unit> {
        return ApiResponse.error(
            ex.status.value().toString(),
            ex.message
        )
    }

}