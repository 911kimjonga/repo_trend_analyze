package com.repo.auth.user.service

import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
import com.repo.auth.user.model.dto.response.UserResponseDto
import com.repo.auth.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val repository: UserRepository,
) {

    fun saveUser(
        dto: SaveRequestDto
    ): Boolean {
        return repository.save(
            requestDto = dto
        )
            .insertedCount > 0
    }

    fun updateUser(
        dto: UpdateRequestDto
    ): Boolean {
        return repository.update(
            requestDto = dto
        ) > 0
    }

    fun findUser(
        id: Long
    ): UserResponseDto {
        return repository.findById(
            id = id
        )
    }

    fun findUser(
        userName: String
    ): UserResponseDto {
        return repository.findByUsername(
            username = userName
        )
    }

}