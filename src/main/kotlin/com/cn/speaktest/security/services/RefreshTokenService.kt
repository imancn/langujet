package com.cn.speaktest.security.services

import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.repository.user.RefreshTokenRepository
import com.cn.speaktest.repository.user.UserRepository
import com.cn.speaktest.security.exception.TokenRefreshException
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
            throw TokenRefreshException(token.token, "Refresh token was expired. Please make a new signin request")
        }
        return token
    }

    @Transactional
    fun deleteByUsername(username: String): Int {
        val user = userRepository.findByUsername(username) ?: throw RuntimeException("User Not Found with username: $username")
        return refreshTokenRepository.deleteByUser(user)
    }
}