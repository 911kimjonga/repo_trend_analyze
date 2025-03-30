package com.repo.security.user.model.dto.response

data class SignInResponseDto(
    val id: String,
    val username: String,
    val password: String,
    val userRole: String,
    val status: String,
)