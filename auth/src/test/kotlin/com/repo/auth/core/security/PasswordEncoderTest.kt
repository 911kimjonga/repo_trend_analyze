package com.repo.auth.core.security

import com.repo.common.logs.logInfo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class PasswordEncoderTest {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun passwordEncoder() {

        val password = "1234"

        val encodedPassword = passwordEncoder.encode(password)

        logInfo("encodedPassword: $encodedPassword")

        assertTrue(passwordEncoder.matches(password, encodedPassword))

    }

}