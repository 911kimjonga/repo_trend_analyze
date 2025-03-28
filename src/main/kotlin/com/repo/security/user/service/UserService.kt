package com.repo.security.user.service

import com.repo.security.user.model.dto.UserRequestDto
import com.repo.security.user.model.dto.UserResponseDto
import com.repo.security.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
) {

    @Transactional
    fun saveUser(userRequest: UserRequestDto): Boolean {
        return repository.saveUser(userRequest).isIgnore.not()
    }

    @Transactional
    fun findUserByUserName(userName: String): UserResponseDto {
        val userCount = repository.countByUserName(userName)
        val userEntity = repository.findByUserName(userName).single()
        return UserResponseDto(
            userEntity.userName,
            userEntity.userEmail ?: "",
        )
    }
    
}