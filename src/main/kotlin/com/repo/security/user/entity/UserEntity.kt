package com.repo.security.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(pk: EntityID<Long>): LongEntity(pk) {
    companion object : LongEntityClass<UserEntity>(Users)
    var username by Users.username
    var email by Users.email
    var password by Users.password
}