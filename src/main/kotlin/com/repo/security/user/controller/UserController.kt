package com.repo.security.user.controller

import com.repo.security.common.exception.CustomException
import com.repo.security.common.exception.CustomException.*
import com.repo.security.common.jwt.component.JwtComponent
import com.repo.security.common.utils.ApiResponse
import com.repo.security.user.enums.UserRole
import com.repo.security.user.model.dto.request.SignInRequestDto
import com.repo.security.user.model.dto.request.SignUpRequestDto
import com.repo.security.user.model.vo.request.SignInRequestVo
import com.repo.security.user.model.vo.request.SignUpRequestVo
import com.repo.security.user.model.vo.response.SignInResponseVo
import com.repo.security.user.model.vo.response.SignUpResponseVo
import com.repo.security.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val jwtComponent: JwtComponent,
) {

    @PostMapping("/signup")
    fun sign(
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
    fun login(
        @RequestBody vo: SignInRequestVo
    ): ApiResponse<SignInResponseVo> {
        val user = userService.findUser(
            SignInRequestDto(
                vo.username,
                vo.password
            )
        )

        val token = jwtComponent.generateToken(user)

        return ApiResponse.ok(
            SignInResponseVo(
                token = token,
            )
        )
    }

    @PostMapping("/check")
    fun check(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<String> {
        val token = this.extractToken(authHeader)

        if (jwtComponent.validateToken(token, UserRole.USER)) {
            val id = jwtComponent.getIdFromToken(token)
            return ResponseEntity.ok(id)
        }

        return ResponseEntity.ok("failed")
    }

    private fun extractToken(header: String): String {
        if (!header.startsWith("Bearer ")) {
            throw UnauthorizedException()
        }
        return header.removePrefix("Bearer ").trim()
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        e: CustomException
    ): ApiResponse<Unit> {
        return ApiResponse.error(
            e.status.value().toString(),
            e.message
        )
    }

}