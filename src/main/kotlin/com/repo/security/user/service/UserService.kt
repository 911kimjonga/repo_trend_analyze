package com.repo.security.user.service

import com.repo.security.user.model.dto.SignUpDto
import com.repo.security.user.model.dto.SignInDto
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
    fun saveUser(dto: SignUpDto): Boolean {
        val encryptPassword = passwordEncoder.encode(dto.password)
        return repository.save(
            SignUpDto(
                dto.username,
                encryptPassword,
                dto.email,
            )
        ).insertedCount > 0
    }

    @Transactional
    fun isUser(dto: SignInDto): Boolean {
        val entity = repository.findByUsername(dto.username).singleOrNull() ?: return false
        return passwordEncoder.matches(dto.password, entity.password)
    }

}