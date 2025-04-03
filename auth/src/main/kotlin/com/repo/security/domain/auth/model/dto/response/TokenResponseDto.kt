package com.repo.security.domain.auth.model.dto.response

data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)