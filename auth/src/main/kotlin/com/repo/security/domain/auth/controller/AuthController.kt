package com.repo.security.domain.auth.controller

import com.repo.security.common.utils.ApiResponse
import com.repo.security.core.token.extensions.addCookieRefreshToken
import com.repo.security.core.token.extensions.getRefreshToken
import com.repo.security.domain.auth.model.dto.request.LoginRequestDto
import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.auth.model.vo.request.LoginRequestVo
import com.repo.security.domain.auth.model.vo.request.SignUpRequestVo
import com.repo.security.domain.auth.model.vo.response.LoginResponseVo
import com.repo.security.domain.auth.model.vo.response.RefreshResponseVo
import com.repo.security.domain.auth.model.vo.response.SignUpResponseVo
import com.repo.security.domain.auth.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody vo: SignUpRequestVo
    ): ApiResponse<SignUpResponseVo> {
        val save = authService.signUp(
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
        val token = authService.login(
            LoginRequestDto(
                vo.username,
                vo.password
            )
        )

        response.addCookieRefreshToken(token.refreshToken)

        return ApiResponse.ok(
            LoginResponseVo(
                accessToken = token.accessToken,
            )
        )
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal id: String,
        request: HttpServletRequest,
    ): ApiResponse<Unit> {
        authService.logout(request)

        return ApiResponse.ok()
    }

    @PostMapping("/refresh")
    fun refresh(
        @AuthenticationPrincipal id: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse<RefreshResponseVo> {
        val newToken = authService.refresh(request.getRefreshToken())

        response.addCookieRefreshToken(newToken.refreshToken)

        return ApiResponse.ok(
            RefreshResponseVo(
                accessToken = newToken.accessToken,
            )
        )
    }
}