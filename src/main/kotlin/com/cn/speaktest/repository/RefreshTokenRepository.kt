package com.cn.speaktest.repository

import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.model.security.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {

    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Int
}