package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.user.model.RefreshTokenEntity
import com.cn.langujet.domain.user.repository.RefreshTokenRepository
import com.cn.langujet.domain.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val userRepository: UserRepository,
) {
    @Value("\${app.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs: Long? = null

    fun findByToken(token: String): Optional<RefreshTokenEntity> {
        return refreshTokenRepository.findById(token)
    }
    
    fun createRefreshToken(userId: Long): RefreshTokenEntity {
        var refreshToken = RefreshTokenEntity(
            null,
            userId,
            Date(System.currentTimeMillis() + refreshTokenDurationMs!!)
        )
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }
    
    fun deleteByUserId(userId: Long) {
        if (!userRepository.existsByIdAndDeleted(userId)) { throw UnprocessableException("User Not Found") }
        refreshTokenRepository.deleteByUserId(userId)
    }
    
    fun deleteById(id: String) {
        refreshTokenRepository.deleteById(id)
    }
}