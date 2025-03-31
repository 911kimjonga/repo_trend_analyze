package com.repo.security.domain.auth.controller

import com.repo.security.common.utils.ApiResponse
import com.repo.security.core.jwt.model.JwtRequestDto
import com.repo.security.core.jwt.provider.JwtProvider
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.model.dto.request.SignInRequestDto
import com.repo.security.domain.user.model.dto.request.SignUpRequestDto
import com.repo.security.domain.user.model.vo.request.SignInRequestVo
import com.repo.security.domain.user.model.vo.request.SignUpRequestVo
import com.repo.security.domain.user.model.vo.response.SignInResponseVo
import com.repo.security.domain.user.model.vo.response.SignUpResponseVo
import com.repo.security.domain.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
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

    @PostMapping("/signin")
    fun signIn(
        @RequestBody vo: SignInRequestVo
    ): ApiResponse<SignInResponseVo> {
        val user = userService.findUser(
            SignInRequestDto(
                vo.username,
                vo.password
            )
        )

        val token = jwtProvider.generateToken(
            JwtRequestDto(
                user.id,
                UserRole.fromRole(user.userRole)
            )
        )

        return ApiResponse.ok(
            SignInResponseVo(
                token = token,
            )
        )
    }
}