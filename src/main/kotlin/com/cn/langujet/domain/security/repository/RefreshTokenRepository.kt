package com.cn.langujet.application.security.security.repository

import com.cn.langujet.application.security.security.model.RefreshToken
import com.cn.langujet.application.security.security.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {

    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Int
}