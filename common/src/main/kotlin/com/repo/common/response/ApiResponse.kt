package com.repo.common.response

import kotlinx.serialization.Serializable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Serializable
data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> ok(data: T, message: String = "Success") =
            ApiResponse(
                code = HttpStatus.OK.value().toString(),
                message = message,
                data = data
            )

        fun ok(message: String = "Success") =
            ApiResponse<Unit>(
                code = HttpStatus.OK.value().toString(),
                message = message
            )

        fun auth(message: String = "Auth") =
            ApiResponse<Unit>(
                code = HttpStatus.UNAUTHORIZED.value().toString(),
                message = message
            )


        fun error(code: String, message: String) =
            ApiResponse<Unit>(
                code = code,
                message = message
            )
    }
}