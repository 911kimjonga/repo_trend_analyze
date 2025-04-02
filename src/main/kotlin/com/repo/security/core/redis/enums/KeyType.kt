package com.repo.security.core.redis.enums

enum class KeyType(
    val type: String,
) {
    REFRESH("refresh"),
    BLACKLIST("blacklist"),
    ;
}