package com.repo.security.user.model.dto

data class SignDto(
    val username: String,
    val password: String,
    val email: String,
)