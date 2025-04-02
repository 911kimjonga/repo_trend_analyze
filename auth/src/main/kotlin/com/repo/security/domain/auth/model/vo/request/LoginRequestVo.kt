package com.repo.security.domain.auth.model.vo.request

data class LoginRequestVo(
    val username: String,
    val password: String,
)