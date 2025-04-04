package com.repo.auth.user.model.dto.request

import com.repo.auth.user.enums.UserStatus

data class UpdateRequestDto(
    val userId: String,
    val status: UserStatus
)