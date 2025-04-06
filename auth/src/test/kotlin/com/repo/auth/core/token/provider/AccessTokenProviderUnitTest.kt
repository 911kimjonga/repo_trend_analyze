package com.repo.auth.core.token.provider

import com.repo.auth.common.exception.AuthException
import com.repo.auth.core.redis.enums.AuthRedisKeyType.BLACKLIST
import com.repo.auth.core.redis.service.AuthRedisService
import com.repo.auth.core.token.config.JwtConfig
import com.repo.auth.user.enums.UserRole
import com.repo.common.logs.logInfo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class AccessTokenProviderUnitTest {

    @Mock
    private lateinit var config: JwtConfig

    @Mock
    private lateinit var redisService: AuthRedisService

    @InjectMocks
    lateinit var accessTokenProvider: AccessTokenProvider

    companion object {
        private lateinit var userId: String
        private lateinit var userRole: UserRole
        private lateinit var adminRole: UserRole

        private lateinit var token: String
        private lateinit var overTtlToken: String
        private lateinit var adminToken: String

        private lateinit var header: String

        private var ttl: Long = 0L
    }

    @BeforeEach
    fun setUp() {
        lenient().whenever(config.secret).thenReturn("very-secret-very-secret-very-secret")
        lenient().whenever(config.expiration).thenReturn(3600000000L)

        userId = "0"
        userRole = UserRole.USER
        adminRole = UserRole.ADMIN

        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDM5Mjk1NTYsImV4cCI6MTc0NDI4OTU1Nn0.1VHdlpKgA7L34OrX99lX93nrfvNU1Gy05ebshYMFHMQ"
        overTtlToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDM5MzA3MjAsImV4cCI6MTc0MzkzMDcyMH0.dUJ5emB4c3sINbzfbUSr-WfbMbtDRl_HcWtu5vn2H-E"
        adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzQzOTMxOTA2LCJleHAiOjE3NDc1MzE5MDZ9.svWkGm6I9_w9i-m9K13TN0V2r1b_Q9bqIFvCSq-HSc0"

        header = "Bearer $token"

        ttl = 100000L
    }

    @Test
    fun generate() {

        val userId = userId
        val userRole = adminRole

        val result = accessTokenProvider.generate(userId, userRole)

        logInfo("result: $result")

        assertNotNull(result)

    }

    @Test
    fun parseBearerToken() {

        val header = header

        val result = accessTokenProvider.parseBearerToken(header)

        assertNotNull(result)

    }

    @Test
    fun validateReturn() {

        val token = token
        val expectedRole = userRole

        val result = accessTokenProvider.validate(token, expectedRole)

        assertTrue(result)

    }

    @Test
    fun validateBlackList() {

        val token = token
        val expectedRole = userRole

        whenever(redisService.has(BLACKLIST, token)).thenReturn(true)

        val result = assertThrows<AuthException.AccessTokenException.InvalidAccessTokenException> {
            accessTokenProvider.validate(token, expectedRole)
        }

        assertEquals(result.message, "Invalid access token. Access token is blacklisted")
        verify(redisService).has(BLACKLIST, token)

    }

    @Test
    fun validateTtl() {

        val token = overTtlToken
        val expectedRole = userRole

        val result = assertThrows<AuthException.AccessTokenException.ExpiredAccessTokenException> {
            accessTokenProvider.validate(token, expectedRole)
        }

        assertEquals(result.message, "Expired access token")

    }

    @Test
    fun validateRole() {

        val token = adminToken
        val expectedRole = userRole

        val result = assertThrows<AuthException.AccessTokenException.InvalidRoleException> {
            accessTokenProvider.validate(token, expectedRole)
        }

        assertEquals(result.message, "Invalid roles. ExpectedRole is $expectedRole")

    }

    @Test
    fun getUserId() {

        val token = token

        val result = accessTokenProvider.getUserId(token)

        assertEquals(result, userId)

    }

    @Test
    fun addToBlackList() {

        val userId = userId
        val token = token
        val ttl = ttl

        whenever(redisService.save(BLACKLIST, token, userId, ttl))

        accessTokenProvider.addToBlackList(userId, token)

        verify(redisService).save(BLACKLIST, token, userId, ttl)

    }


}