package com.repo.security.user.model.dto

data class SignUpDto(
    val username: String,
    val password: String,
    val email: String,
)