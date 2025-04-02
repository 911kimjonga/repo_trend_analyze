package com.repo.security.domain.auth.model.vo.request

data class SignUpRequestVo(
    val username: String,
    val password: String,
    val email: String = "",
)