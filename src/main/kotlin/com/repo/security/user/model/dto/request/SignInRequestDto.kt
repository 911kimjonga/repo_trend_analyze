package com.repo.security.user.model.dto.request

data class SignInRequestDto(
    val username: String,
    val password: String,
)