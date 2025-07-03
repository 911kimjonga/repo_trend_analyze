package com.repo.auth.domain.auth.controller

import com.repo.common.response.ApiResponse
import com.repo.auth.core.token.extensions.addCookieRefreshToken
import com.repo.auth.core.token.extensions.getRefreshToken
import com.repo.auth.domain.auth.model.dto.request.LoginRequestDto
import com.repo.auth.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.auth.domain.auth.model.vo.request.LoginRequestVo
import com.repo.auth.domain.auth.model.vo.request.SignUpRequestVo
import com.repo.auth.domain.auth.model.vo.response.TokenResponseVo
import com.repo.auth.domain.auth.model.vo.response.SignUpResponseVo
import com.repo.auth.domain.auth.service.AuthService
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
            dto = SignUpRequestDto(
                username = vo.username,
                enteredPassword = vo.password,
                email = vo.email
            )
        )

        return ApiResponse.ok(
            data = SignUpResponseVo(
                isSuccess = save
            )
        )
    }

    @PostMapping("withdraw")
    fun withdraw(
        @AuthenticationPrincipal id: String,
        request: HttpServletRequest,
    ): ApiResponse<Unit> {
        authService.withdraw(id)
        authService.logout(request)

        return ApiResponse.ok()
    }

    @PostMapping("/login")
    fun login(
        response: HttpServletResponse,
        @RequestBody vo: LoginRequestVo
    ): ApiResponse<TokenResponseVo> {
        val token = authService.login(
            dto = LoginRequestDto(
                username = vo.username,
                password = vo.password
            )
        )

        response.addCookieRefreshToken(
            refreshToken = token.refreshToken
        )

        return ApiResponse.ok(
            data = TokenResponseVo(
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
    ): ApiResponse<TokenResponseVo> {
        val newToken = authService.refresh(
            refreshToken = request.getRefreshToken()
        )

        response.addCookieRefreshToken(
            refreshToken = newToken.refreshToken
        )

        return ApiResponse.ok(
            data = TokenResponseVo(
                accessToken = newToken.accessToken,
            )
        )
    }

}