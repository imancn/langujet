package com.cn.langujet.domain.user.services

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.user.model.RefreshToken
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

    fun findByToken(token: String): Optional<RefreshToken> {
        return refreshTokenRepository.findById(token)
    }

    fun createRefreshToken(userId: String): RefreshToken {
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