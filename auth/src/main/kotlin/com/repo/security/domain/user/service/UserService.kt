package com.repo.security.domain.user.service

import com.repo.security.domain.user.model.dto.request.SaveRequestDto
import com.repo.security.domain.user.model.dto.request.UpdateRequestDto
import com.repo.security.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
) {

    @Transactional
    fun saveUser(
        dto: SaveRequestDto
    ) =
        repository.save(dto).insertedCount > 0

    @Transactional
    fun updateUser(
        dto: UpdateRequestDto
    ) =
        repository.update(dto)

    @Transactional
    fun findUser(
        id: Long
    ) =
        repository.findById(id)

    @Transactional
    fun findUser(
        userName: String
    ) =
        repository.findByUsername(userName)

}