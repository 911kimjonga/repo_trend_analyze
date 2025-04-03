package com.repo.auth.domain.auth.model.dto.request

data class SignUpRequestDto(
    val username: String,
    val enteredPassword: String,
    val email: String,
)