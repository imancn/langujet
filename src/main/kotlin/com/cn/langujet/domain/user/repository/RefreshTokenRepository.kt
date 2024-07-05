package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun deleteByUserId(userId: String)
}