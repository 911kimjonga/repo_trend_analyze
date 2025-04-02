package com.repo.security.domain.auth.model.dto.response

data class LoginResponseDto(
    val id: String,
    val username: String,
    val password: String,
    val userRole: String,
    val status: String,
)