package com.repo.auth.user.repository

import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserRepositoryTest {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var userRepository: UserRepository

    companion object {
        private var initUserId: Long = 1L
        private lateinit var initUsername: String

        private lateinit var saveUsername: String
        private lateinit var savePassword: String
        private lateinit var saveEmail: String

        private lateinit var updateStatus: UserStatus
    }

    @BeforeEach
    fun setUp() {
        initUserId = 1L
        initUsername = "alice123"

        saveUsername = "tester"
        savePassword = passwordEncoder.encode("1234")
        saveEmail = "test@tester.com"

        updateStatus = UserStatus.DEACTIVE
    }


    @Test
    fun save() {

        val username = saveUsername
        val password = savePassword
        val email = saveEmail
        val dto = SaveRequestDto(username, password, email)

        val result = userRepository.save(dto)

        assertEquals(result.insertedCount, 1)

    }

    @Test
    fun update() {

        val id = initUserId.toString()
        val status = updateStatus
        val dto = UpdateRequestDto(id, status)

        val result = userRepository.update(dto)

        assertEquals(result, 1)

    }

    @Test
    fun findById() {

        val id = initUserId

        val result = userRepository.findById(id)

        assertEquals(id.toString(), result.id)

    }

    @Test
    fun countByUsername() {

        val username = initUsername

        val result = userRepository.countByUsername(username)

        assertEquals(result, 1)

    }

    @Test
    fun findByUsername() {

        val username = initUsername

        val result = userRepository.findByUsername(username)

        assertEquals(username, result.username)

    }

}