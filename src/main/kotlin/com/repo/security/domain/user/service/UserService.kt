package com.repo.security.domain.user.service

import com.repo.security.domain.auth.model.dto.request.SignUpRequestDto
import com.repo.security.domain.auth.model.dto.request.LoginRequestDto
import com.repo.security.domain.auth.model.dto.response.LoginResponseDto
import com.repo.security.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun saveUser(requestDto: SignUpRequestDto): Boolean {
        val encryptPassword = passwordEncoder.encode(requestDto.password)
        return repository.save(
            SignUpRequestDto(
                requestDto.username,
                encryptPassword,
                requestDto.email,
            )
        ).insertedCount > 0
    }

    @Transactional
    fun findUser(id: Long): LoginResponseDto {
        val responseDto = repository.findById(id)
        return responseDto
    }

    @Transactional
    fun findUser(requestDto: LoginRequestDto): LoginResponseDto {
        val responseDto = repository.findByUsername(requestDto.username)
        passwordEncoder.matches(requestDto.password, responseDto.password)
        return responseDto
    }

}