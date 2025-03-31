package com.repo.security.core.jwt.model

import com.repo.security.domain.user.enums.UserRole

data class JwtRequestDto(
    val id: String,
    val role: UserRole
)