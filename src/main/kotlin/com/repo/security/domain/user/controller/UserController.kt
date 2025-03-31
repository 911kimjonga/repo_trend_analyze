package com.repo.security.domain.user.controller

import com.repo.security.common.exception.SecurityException
import com.repo.security.common.exception.SecurityException.*
import com.repo.security.core.jwt.provider.JwtProvider
import com.repo.security.common.utils.ApiResponse
import com.repo.security.core.jwt.model.JwtRequestDto
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.model.dto.request.SignInRequestDto
import com.repo.security.domain.user.model.dto.request.SignUpRequestDto
import com.repo.security.domain.user.model.vo.request.SignInRequestVo
import com.repo.security.domain.user.model.vo.request.SignUpRequestVo
import com.repo.security.domain.user.model.vo.response.SignInResponseVo
import com.repo.security.domain.user.model.vo.response.SignUpResponseVo
import com.repo.security.domain.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val jwtComponent: JwtProvider,
) {
    @PostMapping("/check")
    fun check(
        @AuthenticationPrincipal principal: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("user id: $principal")
    }

    @ExceptionHandler(
        SecurityException::class
    )
    fun handleSecurityException(
        e: SecurityException
    ): ApiResponse<Unit> {
        return ApiResponse.error(
            e.status.value().toString(),
            e.message
        )
    }

}