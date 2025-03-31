package com.repo.security.domain.user.enums

enum class UserRole(
    val role: String,
) {
    USER("USER"),
    ADMIN("ADMIN"),
    ;

    companion object {
        fun fromRole(role: String): UserRole {
            return entries.find { it.role == role }
                ?: throw IllegalArgumentException("Unknown user role: $role")
        }
    }
}