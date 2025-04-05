package com.repo.auth.core.redis.enums

enum class AuthRedisKeyType(
    val type: String,
) {
    REFRESH("refresh"),
    BLACKLIST("blacklist"),
    ;
}