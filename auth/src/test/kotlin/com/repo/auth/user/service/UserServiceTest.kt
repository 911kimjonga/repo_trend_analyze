package com.repo.auth.user.service

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
import kotlin.test.assertTrue

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserServiceTest {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userService: UserService

    @Test
    fun saveUser() {

        val dto = SaveRequestDto("tester", passwordEncoder.encode("1234"), "tester@tester.com")

        val save = userService.saveUser(dto)

        assertTrue(save)

    }

    @Test
    fun updateUser() {

        val dto = UpdateRequestDto("1", UserStatus.DEACTIVE)

        val update = userService.updateUser(dto)

        assertTrue(update)

    }

    @Test
    fun findUserById() {

        val id = 1L

        val user = userService.findUser(id)

        assertEquals(id.toString(), user.id)

    }

    @Test
    fun findUserByUsername() {

        val username = "alice123"

        val user = userService.findUser(username)

        assertEquals(username, user.username)

    }

}