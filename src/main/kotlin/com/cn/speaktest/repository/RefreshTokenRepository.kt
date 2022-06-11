package com.cn.speaktest.repository

import com.cn.speaktest.model.RefreshToken
import com.cn.speaktest.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {

    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Int
}