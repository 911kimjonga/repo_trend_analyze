package com.repo.security.domain.auth.model.dto.request

data class SignUpRequestDto(
    val username: String,
    val password: String,
    val email: String,
)