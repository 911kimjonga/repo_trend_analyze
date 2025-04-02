package com.repo.security.common.exception

import com.repo.security.common.utils.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SecurityApiExceptionHandlerAdvice {

    @ExceptionHandler(
        SecurityException::class
    )
    fun handleSecurityException(
        ex: SecurityException
    ): ApiResponse<Unit> {
        return ApiResponse.error(
            ex.status.value().toString(),
            ex.message
        )
    }

    @ExceptionHandler(
        Exception::class
    )
    fun handleException(
        ex: Exception
    ): ApiResponse<Unit> {
        return ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value().toString(),
            ex.message.orEmpty()
        )
    }

}