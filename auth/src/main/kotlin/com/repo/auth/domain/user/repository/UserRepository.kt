package com.repo.auth.domain.user.repository

import com.repo.auth.common.exception.AuthException.*
import com.repo.auth.domain.user.entity.UserEntity
import com.repo.auth.domain.user.enums.UserRole
import com.repo.auth.domain.user.enums.UserStatus
import com.repo.auth.domain.user.model.dto.request.SaveRequestDto
import com.repo.auth.domain.user.model.dto.request.UpdateRequestDto
import com.repo.auth.domain.user.model.dto.response.UserResponseDto
import com.repo.auth.domain.user.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun save(requestDto: SaveRequestDto): InsertStatement<Long> {
        return Users.insertIgnore {
            it[username] = requestDto.username
            it[password] = requestDto.encryptedPassword
            it[email] = requestDto.email
            it[userRole] = UserRole.USER.role
            it[status] = UserStatus.ACTIVE.status
        }
    }

    fun update(requestDto: UpdateRequestDto): Int {
        return Users.update({ Users.id eq requestDto.userId.toLong() }) {
            it[status] = requestDto.status.status
        }
    }

    fun findById(id: Long): UserResponseDto {
        val entity = UserEntity.find {
            (Users.id eq id) and
                    (Users.status eq UserStatus.ACTIVE.status)
        }.singleOrNull() ?: throw UnauthenticatedException()

        return UserResponseDto(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.email,
            entity.userRole,
            entity.status
        )
    }

    fun countByUsername(username: String): Long {
        return Users.select(intLiteral(1))
            .where { Users.username eq username }
            .count()
    }

    fun findByUsername(username: String): UserResponseDto {
        val entity = UserEntity.find {
            (Users.username eq username) and
                    (Users.status eq UserStatus.ACTIVE.status)
        }.singleOrNull() ?: throw UnauthenticatedException()

        return UserResponseDto(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.email,
            entity.userRole,
            entity.status
        )
    }

}