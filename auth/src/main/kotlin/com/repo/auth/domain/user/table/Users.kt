package com.repo.auth.domain.user.table

import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable("users") {
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
    val email = varchar("email", 255)
    val userRole = varchar("user_role", 16)
    val status = varchar("status", 1)
}