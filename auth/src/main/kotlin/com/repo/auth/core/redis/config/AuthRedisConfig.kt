package com.repo.auth.core.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class AuthRedisConfig {

    @Bean
    fun authRedisConnectionFactory(
    ): RedisConnectionFactory {
        return LettuceConnectionFactory()
    }

    @Bean
    fun authRedisTemplate(
    ): StringRedisTemplate {
        return StringRedisTemplate(authRedisConnectionFactory())
    }

}