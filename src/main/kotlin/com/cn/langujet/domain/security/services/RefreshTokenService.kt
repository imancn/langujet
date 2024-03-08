package com.cn.langujet.domain.security.services

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.security.model.RefreshToken
import com.cn.langujet.domain.security.repository.RefreshTokenRepository
import com.cn.langujet.domain.security.repository.UserRepository
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

    fun findByToken(token: String): Optional<RefreshToken> {
        return refreshTokenRepository.findById(token)
    }

    fun createRefreshToken(userId: String): RefreshToken {
        deleteByUserId(userId)
        var refreshToken = RefreshToken(
            null,
            userId,
            Date(System.currentTimeMillis() + refreshTokenDurationMs!!)
        )
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun deleteByUserId(userId: String): Int {
        if (!userRepository.existsById(userId)) { throw NotFoundException("User Not Found") }
        return refreshTokenRepository.deleteByUserId(userId)
    }
}