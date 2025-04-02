package com.repo.security.domain.auth.model.vo.response

data class LoginResponseVo(
    val accessToken: String,
    val refreshToken: String,
)