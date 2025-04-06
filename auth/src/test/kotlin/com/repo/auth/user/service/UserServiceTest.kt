package com.repo.auth.user.service

import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserServiceTest {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userService: UserService

    companion object {
        private lateinit var saveUsername: String
        private lateinit var savePassword: String
        private lateinit var saveEmail: String

        private lateinit var updateStatus: UserStatus

        private var initUserId: Long = 1L
        private lateinit var initUsername: String
    }

    @BeforeEach
    fun setUp() {
        initUserId = 1
        initUsername = "alice123"

        saveUsername = "tester"
        savePassword = passwordEncoder.encode("1234")
        saveEmail = "test@tester.com"

        updateStatus = UserStatus.DEACTIVE
    }

    @Test
    fun saveUser() {

        val username = saveUsername
        val password = savePassword
        val email = saveEmail
        val dto = SaveRequestDto(username, password, email)

        val result = userService.saveUser(dto)

        assertTrue(result)

    }

    @Test
    fun updateUser() {

        val id = initUserId.toString()
        val status = updateStatus
        val dto = UpdateRequestDto(id, status)

        val result = userService.updateUser(dto)

        assertTrue(result)

    }

    @Test
    fun findUserById() {

        val id = initUserId

        val result = userService.findUser(id)

        assertEquals(id.toString(), result.id)

    }

    @Test
    fun findUserByUsername() {

        val username = initUsername

        val result = userService.findUser(username)

        assertEquals(username, result.username)

    }

}