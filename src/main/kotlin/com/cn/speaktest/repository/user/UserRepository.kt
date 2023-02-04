package com.cn.speaktest.repository.user

import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String?): Boolean
}