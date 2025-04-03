package com.repo.auth.core.token.enums

enum class AccessTokenClaims(
    val claim: String,
) {
    ROLE("role"),
    ;
}