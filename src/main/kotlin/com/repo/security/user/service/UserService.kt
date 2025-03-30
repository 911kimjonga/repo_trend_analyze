package com.repo.security.user.service

import com.repo.security.user.model.dto.request.SignUpRequestDto
import com.repo.security.user.model.dto.request.SignInRequestDto
import com.repo.security.user.model.dto.response.SignInResponseDto
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
    fun findUser(requestDto: SignInRequestDto): SignInResponseDto {
        val responseDto = repository.findByUsername(requestDto.username)
        passwordEncoder.matches(requestDto.password, responseDto.password)
        return responseDto
    }

}