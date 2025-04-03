package com.repo.auth.core.token.model

import com.repo.auth.domain.user.enums.UserRole

data class AccessTokenRequestDto(
    val id: String,
    val role: UserRole
)