package com.repo.security.domain.user.model.vo.request

data class SignUpRequestVo(
    val username: String,
    val password: String,
    val email: String = "",
)