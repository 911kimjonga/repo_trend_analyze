package com.repo.security.domain.auth.service

import com.repo.security.core.redis.enums.KeyType
import com.repo.security.core.redis.service.RedisService
import com.repo.security.core.token.provider.AccessTokenProvider
import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.user.service.UserService
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class AuthService(
    private val userService: UserService,
    private val redisService: RedisService,
    private val accessTokenProvider: AccessTokenProvider,
) {

    fun signUp(dto: SignUpRequestDto) =
        userService.saveUser(dto)

    fun signOut() {

    }

    fun login() {

    }

    fun logout() {

    }

    fun saveBlackList(token: String) {
        val ttl = accessTokenProvider.getRemainingTime(token)
        return redisService.save(KeyType.BLACKLIST, token, "logout", ttl)
    }

    fun isBlackList(token: String): Boolean {
        return redisService.has(KeyType.BLACKLIST, token)
    }

}