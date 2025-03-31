package com.repo.security.domain.user.model.dto.request

data class SignUpRequestDto(
    val username: String,
    val password: String,
    val email: String,
)