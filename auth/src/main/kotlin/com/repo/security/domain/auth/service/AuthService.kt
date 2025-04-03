package com.repo.security.domain.auth.service

import com.repo.security.common.exception.SecurityException.UnauthenticatedException
import com.repo.security.core.redis.enums.KeyType
import com.repo.security.core.redis.service.RedisService
import com.repo.security.core.token.extensions.getRefreshToken
import com.repo.security.core.token.provider.AccessTokenProvider
import com.repo.security.core.token.provider.RefreshTokenProvider
import com.repo.security.domain.auth.model.dto.request.LoginRequestDto
import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.auth.model.dto.response.TokenResponseDto
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.enums.UserStatus
import com.repo.security.domain.user.model.dto.request.SaveRequestDto
import com.repo.security.domain.user.model.dto.request.UpdateRequestDto
import com.repo.security.domain.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
    private val redisService: RedisService,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
) {

    fun signUp(
        dto: SignUpRequestDto
    ) =
        userService.saveUser(
            SaveRequestDto(
                dto.username,
                passwordEncoder.encode(dto.enteredPassword),
                dto.email
            )
        )

    fun withdraw(
        userId: String,
    ) {
        userService.updateUser(
            UpdateRequestDto(
                userId,
                UserStatus.DEACTIVE
            )
        )
    }

    fun login(
        dto: LoginRequestDto
    ): TokenResponseDto {
        val user = userService.findUser(dto.username)
        passwordEncoder.matches(dto.password, user.encryptedPassword)

        return TokenResponseDto(
            accessTokenProvider.generateAccessToken(user.id, UserRole.fromRole(user.userRole)),
            refreshTokenProvider.generateRefreshToken(user.id)
        )
    }

    fun logout(
        request: HttpServletRequest,
    ) {
        val accessToken: String = accessTokenProvider.extractToken(request)
        val refreshToken: String = request.getRefreshToken()

        accessTokenProvider.saveBlackList(accessToken)
        refreshTokenProvider.deleteRefreshToken(refreshToken)
    }

    fun refresh(
        refreshToken: String
    ) =
        TokenResponseDto(
            refreshTokenProvider.reissueAccessToken(refreshToken),
            refreshTokenProvider.rotateRefreshToken(refreshToken)
        )

}