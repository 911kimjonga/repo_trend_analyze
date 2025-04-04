package com.repo.auth.user.model.dto.request

data class SaveRequestDto(
    val username: String,
    val encryptedPassword: String,
    val email: String,
)