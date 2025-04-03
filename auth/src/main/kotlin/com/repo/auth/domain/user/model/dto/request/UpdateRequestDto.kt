package com.repo.auth.domain.user.model.dto.request

import com.repo.auth.domain.user.enums.UserStatus

data class UpdateRequestDto(
    val userId: String,
    val status: UserStatus
)