package com.cn.speaktest.security.services

import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.advice.RefreshTokenException
import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.repository.user.RefreshTokenRepository
import com.cn.speaktest.repository.user.UserRepository
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