package com.repo.auth.user.repository

import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserRepositoryTest {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun save() {

        val dto = SaveRequestDto("tester", passwordEncoder.encode("1234"), "tester@tester.com")

        val save = userRepository.save(dto)

        assertEquals(save.insertedCount, 1)

    }

    @Test
    fun update() {

        val dto = UpdateRequestDto("1", UserStatus.DEACTIVE)

        val update = userRepository.update(dto)

        assertEquals(update, 1)

    }

    @Test
    fun findById() {

        val id = 1L

        val user = userRepository.findById(id)

        assertEquals(id.toString(), user.id)

    }

    @Test
    fun countByUsername() {

        val username = "alice123"

        val count = userRepository.countByUsername(username)

        assertEquals(count, 1)

    }

    @Test
    fun findByUsername() {

        val username = "alice123"

        val user = userRepository.findByUsername(username)

        assertEquals(username, user.username)

    }

}