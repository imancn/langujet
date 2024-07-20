package com.cn.langujet.domain.user.repository

import com.cn.langujet.domain.user.model.RefreshTokenEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshTokenEntity, String> {
    fun deleteByUserId(userId: String)
}