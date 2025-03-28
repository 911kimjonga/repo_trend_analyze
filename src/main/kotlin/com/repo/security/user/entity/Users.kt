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

object Users : LongIdTable("users", "user_id") {
    val userName = varchar("user_name", 255)
    val userEmail = varchar("user_email", 255).uniqueIndex()
}