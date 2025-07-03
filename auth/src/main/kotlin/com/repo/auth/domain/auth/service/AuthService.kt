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
            dto = SaveRequestDto(
                username = dto.username,
                encryptedPassword = passwordEncoder.encode(dto.enteredPassword),
                email = dto.email
            )
        )

    fun withdraw(userId: String) =
        userService.updateUser(
            dto = UpdateRequestDto(
                userId = userId,
                status = UserStatus.DEACTIVE
            )
        )

    fun login(dto: LoginRequestDto): TokenResponseDto {
        val user = userService.findUser(
            userName = dto.username
        )

        passwordEncoder.matches(
            dto.password,
            user.encryptedPassword
        )

        return TokenResponseDto(
            accessToken = tokenService.generateAccessToken(
                userId = user.id,
                userRole = UserRole.fromRole(user.userRole)
            ),
            refreshToken = tokenService.generateRefreshToken(user.id)
        )
    }

    fun logout(request: HttpServletRequest) {
        val accessToken: String = tokenService.getAccessToken(
            request = request
        )

        val refreshToken: String = tokenService.getRefreshToken(
            request = request
        )

        val userId = tokenService.deleteRefreshToken(
            refreshToken = refreshToken
        )

        tokenService.addAccessTokenToBlacklist(
            userId = userId,
            accessToken = accessToken
        )
    }

    fun refresh(refreshToken: String) =
        TokenResponseDto(
            accessToken = tokenService.reissueAccessToken(refreshToken),
            refreshToken = tokenService.rotateRefreshToken(refreshToken)
        )

}