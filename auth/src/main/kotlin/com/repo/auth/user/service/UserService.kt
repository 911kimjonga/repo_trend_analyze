package com.repo.auth.user.service

import com.repo.auth.user.model.dto.request.SaveRequestDto
import com.repo.auth.user.model.dto.request.UpdateRequestDto
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
    ) =
        repository.save(dto).insertedCount > 0

    fun updateUser(
        dto: UpdateRequestDto
    ) =
        repository.update(dto)

    fun findUser(
        id: Long
    ) =
        repository.findById(id)

    fun findUser(
        userName: String
    ) =
        repository.findByUsername(userName)

}