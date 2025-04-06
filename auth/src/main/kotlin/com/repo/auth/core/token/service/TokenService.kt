package com.repo.auth.core.token.service

import com.repo.auth.core.token.extensions.getAccessTokenHeader
import com.repo.auth.core.token.extensions.getRefreshToken
import com.repo.auth.core.token.provider.AccessTokenProvider
import com.repo.auth.core.token.provider.RefreshTokenProvider
import com.repo.auth.user.enums.UserRole
import com.repo.auth.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val userService: UserService,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider
) {

    fun generateAccessToken(
        userId: String,
        userRole: UserRole,
    ) =
        accessTokenProvider.generate(userId, userRole)

    fun getAccessToken(
        request: HttpServletRequest
    ) =
        accessTokenProvider.parseBearerToken(request.getAccessTokenHeader())

    fun validateAccessToken(
        accessToken: String,
        expectedRole: UserRole
    ) =
        accessTokenProvider.validate(accessToken, expectedRole)

    fun getUserIdByAccessToken(
        accessToken: String
    ) =
        accessTokenProvider.getUserId(accessToken)

    fun reissueAccessToken(
        refreshToken: String
    ): String {
        val userId = refreshTokenProvider.getUserId(refreshToken)

        val user = userService.findUser(userId.toLong())

        return this.generateAccessToken(
            user.id,
            UserRole.fromRole(user.userRole)
        )
    }

    fun addAccessTokenToBlacklist(
        userId: String,
        accessToken: String
    ) =
        accessTokenProvider.addToBlackList(userId, accessToken)

    fun generateRefreshToken(
        userId: String
    ) =
        refreshTokenProvider.generate(userId)

    fun getRefreshToken(
        request: HttpServletRequest
    ) =
        request.getRefreshToken()

    fun deleteRefreshToken(
        refreshToken: String
    ) =
        refreshTokenProvider.delete(refreshToken)

    fun rotateRefreshToken(
        oldToken: String
    ) =
        refreshTokenProvider.rotate(oldToken)


}