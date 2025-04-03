package com.repo.auth.domain.auth.model.dto.response

data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)