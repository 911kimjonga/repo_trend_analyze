package com.repo.security.user.model.vo.request

data class SignUpRequestVo(
    val username: String,
    val password: String,
    val email: String = "",
)