package com.repo.security.user.service

import com.repo.security.common.utils.logInfo
import com.repo.security.user.model.dto.SignDto
import com.repo.security.user.model.dto.UserResponseDto
import com.repo.security.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun saveUser(dto: SignDto): Boolean {

        val encryptPassword = passwordEncoder.encode(dto.password)

        logInfo("encrypt password: $encryptPassword")

        return repository.save(
            SignDto(
                dto.username,
                encryptPassword,
                dto.email,
            )
        ).isIgnore.not()
    }

    @Transactional
    fun findUserByUsername(username: String): UserResponseDto {
        val userEntity = repository.findByUsername(username).single()
        return UserResponseDto(
            userEntity.username,
            userEntity.email,
        )
    }
    
}