package com.cn.speaktest.security.repository

import com.cn.speaktest.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String?): Optional<User>
    fun existsByEmail(email: String?): Boolean
}