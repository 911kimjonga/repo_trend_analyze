package com.repo.auth.domain.user.model.dto.request

data class SaveRequestDto(
    val username: String,
    val encryptedPassword: String,
    val email: String,
)