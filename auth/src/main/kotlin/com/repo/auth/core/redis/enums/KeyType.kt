package com.repo.auth.core.redis.enums

enum class KeyType(
    val type: String,
) {
    REFRESH("refresh"),
    BLACKLIST("blacklist"),
    ;
}