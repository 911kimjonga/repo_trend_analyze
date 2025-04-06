package com.repo.auth.domain.auth.service

import com.repo.auth.core.token.service.TokenService
import com.repo.auth.domain.auth.model.dto.request.LoginRequestDto
import com.repo.auth.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.auth.domain.auth.model.dto.response.TokenResponseDto
import com.repo.auth.user.enums.UserRole
import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import com.repo.auth.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
    private val tokenService: TokenService,
) {

    fun signUp(dto: SignUpRequestDto) =
        userService.saveUser(
            SaveRequestDto(
                dto.username,
                passwordEncoder.encode(dto.enteredPassword),
                dto.email
            )
        )

    fun withdraw(userId: String) =
        userService.updateUser(
            UpdateRequestDto(
                userId,
                UserStatus.DEACTIVE
            )
        )

    fun login(dto: LoginRequestDto): TokenResponseDto {
        val user = userService.findUser(dto.username)
        passwordEncoder.matches(dto.password, user.encryptedPassword)

        return TokenResponseDto(
            tokenService.generateAccessToken(user.id, UserRole.fromRole(user.userRole)),
            tokenService.generateRefreshToken(user.id)
        )
    }

    fun logout(request: HttpServletRequest) {
        val accessToken: String = tokenService.getAccessToken(request)
        val refreshToken: String = tokenService.getRefreshToken(request)

        val userId = tokenService.deleteRefreshToken(refreshToken)
        tokenService.addAccessTokenToBlacklist(userId, accessToken)
    }

    fun refresh(refreshToken: String) =
        TokenResponseDto(
            tokenService.reissueAccessToken(refreshToken),
            tokenService.rotateRefreshToken(refreshToken)
        )

}