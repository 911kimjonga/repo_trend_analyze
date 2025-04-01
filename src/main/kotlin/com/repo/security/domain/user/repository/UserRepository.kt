package com.repo.security.domain.user.repository

import com.repo.security.domain.user.entity.UserEntity
import com.repo.security.domain.user.enums.UserRole
import com.repo.security.domain.user.enums.UserStatus
import com.repo.security.domain.user.model.dto.response.SignInResponseDto
import com.repo.security.domain.user.model.dto.request.SignUpRequestDto
import com.repo.security.domain.user.table.Users
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.intLiteral
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun save(requestDto: SignUpRequestDto): InsertStatement<Long> {
        return Users.insertIgnore {
            it[username] = requestDto.username
            it[password] = requestDto.password
            it[email] = requestDto.email
            it[userRole] = UserRole.USER.role
            it[status] = UserStatus.ACTIVE.status
        }
    }

    fun findById(id: Long): SignInResponseDto {
        val entity = UserEntity.find {
            (Users.id eq id) and
                    (Users.status eq UserStatus.ACTIVE.status)
        }.singleOrNull() ?: throw IllegalArgumentException()

        return SignInResponseDto(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.userRole,
            entity.status
        )
    }

    fun countByUsername(username: String): Long {
        return Users.select(intLiteral(1))
            .where { Users.username eq username }
            .count()
    }

    fun findByUsername(username: String): SignInResponseDto {
        val entity = UserEntity.find {
            (Users.username eq username) and
                    (Users.status eq UserStatus.ACTIVE.status)
        }.singleOrNull() ?: throw IllegalArgumentException()

        return SignInResponseDto(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.userRole,
            entity.status
        )
    }

}