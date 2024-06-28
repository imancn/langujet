package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User, String> {
    fun findByEmailAndDeleted(email: String, deleted: Boolean = false): Optional<User>
    fun existsByEmailAndDeleted(email: String, deleted: Boolean = false): Boolean
}