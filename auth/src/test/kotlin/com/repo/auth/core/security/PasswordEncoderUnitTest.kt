package com.repo.auth.core.security

import com.repo.common.logs.logInfo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class PasswordEncoderUnitTest {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    companion object {
        private lateinit var password: String
    }

    @BeforeEach
    fun setup() {
        password = "1234"
    }

    @Test
    fun passwordEncoder() {

        val encodedPassword = passwordEncoder.encode(password)

        val result = passwordEncoder.matches(password, encodedPassword)

        logInfo("encodedPassword: $encodedPassword")

        assertTrue(result)
        verify(passwordEncoder).encode(encodedPassword)

    }

}