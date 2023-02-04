package com.cn.speaktest.repository.user

import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String?): Optional<User>
    fun existsByEmail(email: String?): Boolean
}