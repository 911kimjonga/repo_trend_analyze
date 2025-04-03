package com.repo.auth.domain.auth.model.dto.request

data class LoginRequestDto(
    val username: String,
    val password: String,
)