package com.repo.auth.user.service

import com.repo.auth.user.entity.UserEntity
import com.repo.auth.user.enums.UserRole
import com.repo.auth.user.enums.UserStatus
import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import com.repo.auth.user.model.dto.response.UserResponseDto
import com.repo.auth.user.repository.UserRepository
import com.repo.auth.user.table.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class UserServiceUnitTest {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserService

    companion object {
        private var initUserId: Long = 0L
        private lateinit var initUsername: String
        private lateinit var initEmail: String
        private lateinit var initPassword: String
        private lateinit var initUserRole: UserRole
        private lateinit var initStatus: UserStatus

        private lateinit var initUser: UserEntity
        private lateinit var initUserDto: UserResponseDto

        private lateinit var saveUsername: String
        private lateinit var savePassword: String
        private lateinit var saveEmail: String

        private lateinit var updateStatus: UserStatus
    }

    @BeforeEach
    fun setup() {
        initUserId = 1L
        initUsername = "alice123"
        initPassword = passwordEncoder.encode("1234")
        initEmail = "alice123@test.com"
        initUserRole = UserRole.USER
        initStatus = UserStatus.ACTIVE

        initUserDto = UserResponseDto(
            initUserId.toString(),
            initUsername,
            initPassword,
            initEmail,
            initUserRole.role,
            initStatus.status
        )

        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.drop(Users)
            SchemaUtils.create(Users)

            initUser = UserEntity.new {
                username = initUsername
                email = initEmail
                password = initPassword
                userRole = initUserRole.role
                status = initStatus.status
            }
        }

        saveUsername = "tester"
        savePassword = passwordEncoder.encode("1234")
        saveEmail = "test@test.com"

        updateStatus = UserStatus.DEACTIVE
    }


    @Test
    fun saveUser() {

        val username = saveUsername
        val password = savePassword
        val email = saveEmail
        val dto = SaveRequestDto(username, password, email)

        val insertStatement = mock<InsertStatement<Long>> {
            on { insertedCount } doReturn 1
        }

        whenever(userRepository.save(dto)).thenReturn(insertStatement)

        val result = transaction { userService.saveUser(dto) }

        assertTrue(result)
        verify(userRepository).save(dto)

    }

    @Test
    fun updateUser() {

        val id = initUserId.toString()
        val status = updateStatus
        val dto = UpdateRequestDto(id, status)

        whenever(userRepository.update(dto)).thenReturn(1)

        val result = transaction { userService.updateUser(dto) }

        assertTrue(result)
        verify(userRepository).update(dto)

    }

    @Test
    fun findUserById() {

        val id = initUserId
        val userDto = initUserDto

        whenever(userRepository.findById(id)).thenReturn(userDto)

        val result = transaction { userService.findUser(id) }

        assertEquals(id.toString(), result.id)
        verify(userRepository).findById(id)

    }

    @Test
    fun findUserByUsername() {

        val username = initUsername
        val userDto = initUserDto

        whenever(userRepository.findByUsername(username)).thenReturn(userDto)

        val result = transaction { userService.findUser(username) }

        assertEquals(username, result.username)
        verify(userRepository).findByUsername(username)

    }

}