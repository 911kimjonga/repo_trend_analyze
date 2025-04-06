package com.repo.auth.domain.auth

import com.repo.auth.core.token.service.TokenService
import com.repo.auth.domain.auth.model.dto.request.LoginRequestDto
import com.repo.auth.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.auth.domain.auth.service.AuthService
import com.repo.auth.user.enums.UserRole
import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import com.repo.auth.user.model.dto.response.UserResponseDto
import com.repo.auth.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class AuthServiceUnitTest {

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var tokenService: TokenService

    @Mock
    private lateinit var request: HttpServletRequest

    @InjectMocks
    lateinit var authService: AuthService

    companion object {
        private lateinit var userId: String
        private lateinit var username: String
        private lateinit var password: String
        private lateinit var email: String
        private lateinit var userRole: UserRole
        private lateinit var status: UserStatus

        private lateinit var user: UserResponseDto

        private lateinit var accessToken: String
        private lateinit var refreshToken: String
    }

    @BeforeEach
    fun setup() {
        userId = "1"
        username = "test"
        password = "1234"
        email = "test@test.com"
        userRole = UserRole.USER
        status = UserStatus.ACTIVE

        user = UserResponseDto(userId, username, password, email, userRole.role, status.status)

        accessToken = "accessToken"
        refreshToken = "refreshToken"
    }

    @Test
    fun signUp() {

        val dto = SignUpRequestDto(username, password, email)
        val saveDto = SaveRequestDto(dto.username, dto.enteredPassword, dto.email)

        whenever(userService.saveUser(saveDto)).thenReturn(true)
        whenever(passwordEncoder.encode(dto.enteredPassword)).thenReturn(dto.enteredPassword)

        val result = authService.signUp(dto)

        assertTrue(result)
        verify(userService).saveUser(saveDto)

    }

    @Test
    fun withdraw() {

        val id = userId
        val dto = UpdateRequestDto(userId, UserStatus.DEACTIVE)

        whenever(userService.updateUser(dto)).thenReturn(true)

        val result = authService.withdraw(id)

        assertTrue(result)
        verify(userService).updateUser(dto)
    }

    @Test
    fun login() {

        val dto = LoginRequestDto(username, password)

        whenever(userService.findUser(dto.username)).thenReturn(user)
        whenever(passwordEncoder.matches(password, user.encryptedPassword)).thenReturn(true)
        whenever(tokenService.generateAccessToken(user.id, UserRole.fromRole(user.userRole))).thenReturn(accessToken)
        whenever(tokenService.generateRefreshToken(user.id)).thenReturn(refreshToken)

        val result = authService.login(dto)

        assertEquals(result.accessToken, accessToken)
        assertEquals(result.refreshToken, refreshToken)

        verify(userService).findUser(dto.username)
        verify(passwordEncoder).matches(password, user.encryptedPassword)
        verify(tokenService).generateAccessToken(user.id, UserRole.fromRole(user.userRole))
        verify(tokenService).generateRefreshToken(user.id)

    }

    @Test
    fun logout() {

        val request = request

        whenever(tokenService.getAccessToken(request)).thenReturn(accessToken)
        whenever(tokenService.getRefreshToken(request)).thenReturn(refreshToken)
        whenever(tokenService.deleteRefreshToken(refreshToken)).thenReturn(userId)
        whenever(tokenService.addAccessTokenToBlacklist(userId, accessToken)).then {  }

        val result = authService.logout(request)

        verify(tokenService).getAccessToken(request)
        verify(tokenService).getRefreshToken(request)
        verify(tokenService).deleteRefreshToken(refreshToken)
        verify(tokenService).addAccessTokenToBlacklist(userId, accessToken)

    }

    @Test
    fun refresh() {

        val token = refreshToken

        whenever(tokenService.reissueAccessToken(token)).thenReturn(accessToken)
        whenever(tokenService.rotateRefreshToken(token)).thenReturn(refreshToken)

        val result = authService.refresh(token)

        assertEquals(result.accessToken, accessToken)
        assertEquals(result.refreshToken, refreshToken)

        verify(tokenService).reissueAccessToken(token)
        verify(tokenService).rotateRefreshToken(token)

    }

}