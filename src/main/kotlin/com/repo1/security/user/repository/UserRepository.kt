package com.repo1.security.user.repository

import com.repo1.security.user.entity.UserEntity
import com.repo1.security.user.entity.Users
import com.repo1.security.user.model.dto.UserRequestDto
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.intLiteral
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun saveUser(user: UserRequestDto): InsertStatement<Long> {
        return Users.insertIgnore {
            it[userName] = user.userName
            it[userEmail] = user.userEmail
        }
    }

    fun findByUserName(userName: String): SizedIterable<UserEntity> {
        return UserEntity.find {
            Users.userName eq userName
        }
    }

    fun countByUserName(userName: String): Long {
        return Users.select(intLiteral(1))
            .where { Users.userName eq userName }
            .count()
    }


}