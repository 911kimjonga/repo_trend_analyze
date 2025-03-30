package com.repo.security.common.utils

import kotlinx.serialization.Serializable
import org.springframework.http.HttpStatus

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

        fun error(code: String, message: String) =
            ApiResponse<Unit>(
                code = code,
                message = message
            )
    }
}