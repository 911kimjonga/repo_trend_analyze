package com.repo.auth.user.enums

import com.repo.auth.common.exception.AuthException.AccessTokenException.InvalidRoleException

enum class UserRole(
    val role: String,
) {
    USER("USER"),
    ADMIN("ADMIN"),
    GUEST("GUEST"),
    ;

    companion object {
        fun fromRole(role: String?): UserRole {
            return entries.find { it.role == role }
                ?: throw InvalidRoleException("Unknown user role: $role")
        }
    }
}