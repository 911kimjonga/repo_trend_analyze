package com.repo.security.core.token.model

import com.repo.security.domain.user.enums.UserRole

data class AccessTokenRequestDto(
    val id: String,
    val role: UserRole
)