package com.repo.security.user.model.vo

data class SignVo(
    val username: String,
    val password: String,
    val email: String = "",
)