package com.repo.common.exception

import com.repo.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalApiExceptionHandlerAdvice {

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