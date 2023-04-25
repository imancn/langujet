package com.cn.speaktest.application.security.security.repository

import com.cn.speaktest.application.security.security.model.RefreshToken
import com.cn.speaktest.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {

    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Int
}