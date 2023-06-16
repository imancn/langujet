package com.cn.langujet.domain.security.services

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.advice.RefreshTokenException
import com.cn.langujet.application.security.security.model.RefreshToken
import com.cn.langujet.domain.security.repository.RefreshTokenRepository
import com.cn.langujet.domain.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val userRepository: UserRepository,
) {
    @Value("\${app.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs: Long? = null

    fun findByToken(token: String?): Optional<RefreshToken> {
        return refreshTokenRepository.findByToken(token!!)
    }

    fun createRefreshToken(userId: String): RefreshToken {
        var refreshToken = RefreshToken(
            null,
            userRepository.findById(userId).get(),
            UUID.randomUUID().toString(),
            Instant.now().plusMillis(refreshTokenDurationMs!!),
        )
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw RefreshTokenException("Refresh token was expired. Please make a new sign-in request")
        }
        return token
    }

    @Transactional
    fun deleteByUserId(userId: String): Int {
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User Not Found") }
        return refreshTokenRepository.deleteByUser(user)
    }
}