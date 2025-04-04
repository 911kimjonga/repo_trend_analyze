package com.repo.auth.user.model.dto.response

data class UserResponseDto(
    val id: String,
    val username: String,
    val encryptedPassword: String,
    val email: String,
    val userRole: String,
    val status: String
)