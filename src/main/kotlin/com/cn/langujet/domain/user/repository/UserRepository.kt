package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<UserEntity, String> {
    fun findByEmailAndDeleted(email: String, deleted: Boolean = false): Optional<UserEntity>
    fun existsByEmailAndDeleted(email: String, deleted: Boolean = false): Boolean
    fun findByIdAndDeleted(userId: String, deleted: Boolean = false): Optional<UserEntity>
    fun existsByIdAndDeleted(userId: String, deleted: Boolean = false): Boolean
}