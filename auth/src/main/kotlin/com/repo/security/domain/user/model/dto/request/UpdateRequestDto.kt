package com.repo.security.domain.user.model.dto.request

import com.repo.security.domain.user.enums.UserStatus

data class UpdateRequestDto(
    val userId: String,
    val status: UserStatus
)