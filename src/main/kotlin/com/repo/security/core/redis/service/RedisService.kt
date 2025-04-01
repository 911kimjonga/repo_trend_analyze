package com.repo.security.core.redis.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate
) {
    fun save(userId: String, token: String, expiry: Duration) {
        redisTemplate.opsForValue().set("refresh:$userId", token, expiry)
    }

    fun get(userId: String): String? {
        return redisTemplate.opsForValue().get("refresh:$userId")
    }

    fun delete(userId: String) {
        redisTemplate.delete("refresh:$userId")
    }
}