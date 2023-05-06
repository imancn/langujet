package com.cn.speaktest.application.security.security.repository

import com.cn.speaktest.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String?): Optional<User>
    fun existsByEmail(email: String?): Boolean
}