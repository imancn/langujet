package com.cn.langujet.domain.security.repository

import com.cn.langujet.domain.security.model.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun deleteByUserId(userId: String): Int
}