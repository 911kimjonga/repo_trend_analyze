package com.repo.security.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(pk: EntityID<Long>): LongEntity(pk) {
    companion object : LongEntityClass<UserEntity>(Users)
    var userName by Users.userName
    var userEmail by Users.userEmail
}