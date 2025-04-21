package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<UserEntity, Long> {
    fun findByUsernameAndDeleted(standardEmail: String, deleted: Boolean = false): Optional<UserEntity>
    fun existsByUsernameAndDeleted(standardEmail: String, deleted: Boolean = false): Boolean
    fun findByIdAndDeleted(id: Long, deleted: Boolean = false): Optional<UserEntity>
    fun existsByIdAndDeleted(id: Long, deleted: Boolean = false): Boolean
}