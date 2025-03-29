package com.repo.security.user.repository

import com.repo.security.user.entity.UserEntity
import com.repo.security.user.entity.Users
import com.repo.security.user.model.dto.SignUpDto
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.intLiteral
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun save(dto: SignUpDto): InsertStatement<Long> {
        return Users.insertIgnore {
            it[username] = dto.username
            it[password] = dto.password
            it[email] = dto.email
        }
    }

    fun countByUsername(username: String): Long {
        return Users.select(intLiteral(1))
            .where { Users.username eq username }
            .count()
    }

    fun findByUsername(username: String): SizedIterable<UserEntity> {
        return UserEntity.find {
            Users.username eq username
        }
    }

}