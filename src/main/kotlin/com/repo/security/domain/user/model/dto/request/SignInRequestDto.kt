package com.repo.security.domain.user.model.dto.request

data class SignInRequestDto(
    val username: String,
    val password: String,
)