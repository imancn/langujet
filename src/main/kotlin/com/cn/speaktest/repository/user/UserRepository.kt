package com.cn.speaktest.repository.user

import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String?): User?
    fun findByEmail(email: String?): User?
    fun existsByUsername(username: String?): Boolean
    fun existsByEmail(email: String?): Boolean
}