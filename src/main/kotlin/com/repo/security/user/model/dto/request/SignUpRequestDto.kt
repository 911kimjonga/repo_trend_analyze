package com.repo.security.user.model.dto.request

data class SignUpRequestDto(
    val username: String,
    val password: String,
    val email: String,
)