package com.repo.auth.domain.auth.model.vo.request

data class SignUpRequestVo(
    val username: String,
    val password: String,
    val email: String = "",
)