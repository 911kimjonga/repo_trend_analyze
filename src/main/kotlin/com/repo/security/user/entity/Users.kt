package com.repo.security.user.entity

import org.jetbrains.exposed.dao.id.LongIdTable

/*
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0,

    @Column(nullable = false, length = 255)
    val userName: String = "",

    @Column(nullable = true, length = 255)
    val userEmail: String? = null,
)
 */

object Users : LongIdTable("users") {
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
    val email = varchar("email", 255)
}