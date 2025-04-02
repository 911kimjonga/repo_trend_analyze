package com.repo.security.domain.auth.controller

import com.repo.security.common.utils.ApiResponse
import com.repo.security.core.token.extensions.addCookieRefreshToken
import com.repo.security.core.token.provider.AccessTokenProvider
import com.repo.security.core.token.provider.RefreshTokenProvider
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.auth.model.dto.request.LoginRequestDto
import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.auth.model.vo.request.LoginRequestVo
import com.repo.security.domain.auth.model.vo.request.SignUpRequestVo
import com.repo.security.domain.auth.model.vo.response.LoginResponseVo
import com.repo.security.domain.auth.model.vo.response.RefreshResponseVo
import com.repo.security.domain.auth.model.vo.response.SignUpResponseVo
import com.repo.security.domain.user.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody vo: SignUpRequestVo
    ): ApiResponse<SignUpResponseVo> {
        val save = userService.saveUser(
            SignUpRequestDto(
                vo.username,
                vo.password,
                vo.email
            )
        )

        return ApiResponse.ok(
            SignUpResponseVo(
                isSuccess = save
            )
        )
    }

    @PostMapping("/login")
    fun login(
        response: HttpServletResponse,
        @RequestBody vo: LoginRequestVo
    ): ApiResponse<LoginResponseVo> {
        val user = userService.findUser(
            LoginRequestDto(
                vo.username,
                vo.password
            )
        )

        val accessToken = accessTokenProvider.generateAccessToken(
            user.id,
            UserRole.fromRole(user.userRole)
        )

        val refreshToken = refreshTokenProvider.generateRefreshToken(user.id)

        response.addCookieRefreshToken(refreshToken)

        return ApiResponse.ok(
            LoginResponseVo(
                accessToken = accessToken,
            )
        )
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal principal: String,
        @CookieValue("refreshToken") refreshToken: String
    ): ApiResponse<Unit> {
        refreshTokenProvider.deleteRefreshToken(refreshToken)

        return ApiResponse.ok()
    }

    @PostMapping("/refresh")
    fun refresh(
        response: HttpServletResponse,
        @CookieValue("refreshToken") refreshToken: String
    ): ApiResponse<RefreshResponseVo> {
        val newAccessToken = refreshTokenProvider.reissueAccessToken(refreshToken)
        val newRefreshToken = refreshTokenProvider.rotateRefreshToken(refreshToken)

        response.addCookieRefreshToken(newRefreshToken)

        return ApiResponse.ok(
            RefreshResponseVo(
                accessToken = newAccessToken,
            )
        )
    }
}