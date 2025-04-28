package com.cn.langujet.domain.user.repository

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository
import com.cn.langujet.domain.user.model.UserEntity
import java.util.*

interface UserRepository : HistoricalMongoRepository<UserEntity> {
    fun findByUsernameAndDeleted(standardEmail: String, deleted: Boolean = false): Optional<UserEntity>
    fun findByEmailAndDeleted(email: String, deleted: Boolean = false): Optional<UserEntity>
    fun existsByUsernameAndDeleted(standardEmail: String, deleted: Boolean = false): Boolean
    fun findByIdAndDeleted(id: Long, deleted: Boolean = false): Optional<UserEntity>
    fun existsByIdAndDeleted(id: Long, deleted: Boolean = false): Boolean
}