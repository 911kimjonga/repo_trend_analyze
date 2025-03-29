package com.repo.security.user.model.vo

data class SignUpVo(
    val username: String,
    val password: String,
    val email: String = "",
)