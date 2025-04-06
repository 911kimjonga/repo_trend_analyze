package com.repo.auth.core.token.service

import com.repo.auth.common.exception.AuthException
import com.repo.auth.core.token.constants.ACCESS_TOKEN_HEADER_AUTH
import com.repo.auth.core.token.constants.REFRESH_TOKEN_COOKIE
import com.repo.auth.core.token.provider.AccessTokenProvider
import com.repo.auth.core.token.provider.RefreshTokenProvider
import com.repo.auth.user.enums.UserRole
import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.response.UserResponseDto
import com.repo.auth.user.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class TokenServiceUnitTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var accessTokenProvider: AccessTokenProvider

    @Mock
    private lateinit var refreshTokenProvider: RefreshTokenProvider

    @Mock
    lateinit var request: HttpServletRequest

    @InjectMocks
    lateinit var tokenService: TokenService

    companion object {
        private lateinit var accessToken: String
        private lateinit var header: String

        private lateinit var userId: String
        private lateinit var userRole: UserRole

        private lateinit var refreshToken: String
        private lateinit var newRefreshToken: String

        private lateinit var user: UserResponseDto
    }

    @BeforeEach
    fun setup() {
        accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDM5Mjk1NTYsImV4cCI6MTc0NDI4OTU1Nn0.1VHdlpKgA7L34OrX99lX93nrfvNU1Gy05ebshYMFHMQ"
        header = "Bearer $accessToken"

        userId = "1"
        userRole = UserRole.USER

        refreshToken = UUID.randomUUID().toString()
        newRefreshToken = UUID.randomUUID().toString()

        user = UserResponseDto(
            userId, "tester", "1234", "test@test.com", UserRole.USER.role, UserStatus.ACTIVE.status
        )
    }

    @Test
    fun generateAccessToken() {

        val id = userId
        val userRole = userRole

        whenever(accessTokenProvider.generate(id, userRole)).thenReturn(accessToken)

        val result = tokenService.generateAccessToken(id, userRole)

        assertEquals(result, accessToken)
        verify(accessTokenProvider).generate(id, userRole)

    }

    @Test
    fun getAccessToken() {

        val request = request

        whenever(request.getHeader(ACCESS_TOKEN_HEADER_AUTH)).thenReturn(header)
        whenever(accessTokenProvider.parseBearerToken(header)).thenReturn(accessToken)

        val result = tokenService.getAccessToken(request)

        assertEquals(result, accessToken)
        verify(accessTokenProvider).parseBearerToken(header)

    }

    @Test
    fun validateAccessTokenReturn() {

        val token = accessToken
        val userRole = userRole

        whenever(accessTokenProvider.validate(token, userRole)).thenReturn(true)

        val result = tokenService.validateAccessToken(token, userRole)

        assertTrue(result)
        verify(accessTokenProvider).validate(token, userRole)

    }

    @Test
    fun validateAccessTokenBlackList() {

        val token = accessToken
        val userRole = userRole

        whenever(accessTokenProvider.validate(token, userRole)).thenThrow(
            AuthException.AccessTokenException.InvalidAccessTokenException("Invalid access token. Access token is blacklisted")
        )

        val result = assertThrows<AuthException.AccessTokenException.InvalidAccessTokenException> {
            tokenService.validateAccessToken(token, userRole)
        }

        assertEquals(result.message, "Invalid access token. Access token is blacklisted")
        verify(accessTokenProvider).validate(token, userRole)

    }

    @Test
    fun validateAccessTokenTtl() {

        val token = accessToken
        val userRole = userRole

        whenever(accessTokenProvider.validate(token, userRole)).thenThrow(
            AuthException.AccessTokenException.ExpiredAccessTokenException("Expired access token")
        )

        val result = assertThrows<AuthException.AccessTokenException.ExpiredAccessTokenException> {
            tokenService.validateAccessToken(token, userRole)
        }

        assertEquals(result.message, "Expired access token")
        verify(accessTokenProvider).validate(token, userRole)

    }

    @Test
    fun validateAccessTokenRole() {

        val token = accessToken
        val userRole = userRole

        whenever(accessTokenProvider.validate(token, userRole)).thenThrow(
            AuthException.AccessTokenException.InvalidRoleException("Invalid roles. ExpectedRole is $userRole")
        )

        val result = assertThrows<AuthException.AccessTokenException.InvalidRoleException> {
            tokenService.validateAccessToken(token, userRole)
        }

        assertEquals(result.message, "Invalid roles. ExpectedRole is $userRole")
        verify(accessTokenProvider).validate(token, userRole)

    }

    @Test
    fun getUserIdByAccessToken() {

        val token = accessToken

        whenever(accessTokenProvider.getUserId(token)).thenReturn(userId)

        val result = tokenService.getUserIdByAccessToken(token)

        assertEquals(result, userId)
        verify(accessTokenProvider).getUserId(token)

    }

    @Test
    fun reissueAccessToken() {

        val token = refreshToken

        whenever(refreshTokenProvider.getUserId(token)).thenReturn(userId)
        whenever(userService.findUser(userId.toLong())).thenReturn(user)
        whenever(accessTokenProvider.generate(userId, userRole)).thenReturn(accessToken)

        val result = tokenService.reissueAccessToken(token)

        assertEquals(result, accessToken)
        verify(refreshTokenProvider).getUserId(token)
        verify(userService).findUser(userId.toLong())
        verify(accessTokenProvider).generate(userId, userRole)

    }

    @Test
    fun addAccessTokenToBlacklist() {

        val id = userId
        val token = accessToken

        whenever(accessTokenProvider.addToBlackList(id, token)).then {  }

        tokenService.addAccessTokenToBlacklist(id, token)

        verify(accessTokenProvider).addToBlackList(id, token)

    }

    @Test
    fun generateRefreshToken() {

        val id = userId

        whenever(refreshTokenProvider.generate(userId)).thenReturn(refreshToken)

        val result = tokenService.generateRefreshToken(id)

        assertEquals(result, refreshToken)
        verify(refreshTokenProvider).generate(id)

    }

    @Test
    fun getRefreshToken() {

        val request = request

        whenever(request.cookies).thenReturn(arrayOf(Cookie(REFRESH_TOKEN_COOKIE, refreshToken)))

        val result = tokenService.getRefreshToken(request)

        assertEquals(result, refreshToken)

    }

    @Test
    fun deleteRefreshToken() {

        val token = refreshToken

        whenever(refreshTokenProvider.delete(token)).thenReturn(userId)

        val result = tokenService.deleteRefreshToken(token)

        assertEquals(result, userId)
        verify(refreshTokenProvider).delete(token)

    }

    @Test
    fun rotateRefreshToken() {

        val token = refreshToken

        whenever(refreshTokenProvider.rotate(token)).thenReturn(newRefreshToken)

        val result = tokenService.rotateRefreshToken(token)

        assertEquals(result, newRefreshToken)
        verify(refreshTokenProvider).rotate(token)

    }

}