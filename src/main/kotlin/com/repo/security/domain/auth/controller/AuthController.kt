package com.repo.security.domain.auth.controller

import com.repo.security.common.utils.ApiResponse
import com.repo.security.core.token.model.AccessTokenRequestDto
import com.repo.security.core.token.provider.AccessTokenProvider
import com.repo.security.core.token.provider.RefreshTokenProvider
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.auth.model.dto.request.LoginRequestDto
import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.auth.model.vo.request.LoginRequestVo
import com.repo.security.domain.auth.model.vo.request.RefreshRequestVo
import com.repo.security.domain.auth.model.vo.request.SignUpRequestVo
import com.repo.security.domain.auth.model.vo.response.LoginResponseVo
import com.repo.security.domain.auth.model.vo.response.RefreshResponseVo
import com.repo.security.domain.auth.model.vo.response.SignUpResponseVo
import com.repo.security.domain.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        @RequestBody vo: LoginRequestVo
    ): ApiResponse<LoginResponseVo> {
        val user = userService.findUser(
            LoginRequestDto(
                vo.username,
                vo.password
            )
        )

        val accessToken = accessTokenProvider.generateAccessToken(
            AccessTokenRequestDto(
                user.id,
                UserRole.fromRole(user.userRole)
            )
        )

        val refreshToken = refreshTokenProvider.generateRefreshToken(user.id)

        return ApiResponse.ok(
            LoginResponseVo(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        )
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody vo: LoginRequestVo
    ) {

    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody vo: RefreshRequestVo
    ): ApiResponse<RefreshResponseVo> {
        val newAccessToken = refreshTokenProvider.reissueAccessToken(vo.refreshToken)
        val newRefreshToken = refreshTokenProvider.rotateRefreshToken(vo.refreshToken)

        return ApiResponse.ok(
            RefreshResponseVo(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
            )
        )
    }
}