package com.cn.langujet.domain.user.services

import com.cn.langujet.domain.user.model.UserEntity
import com.cn.langujet.domain.user.repository.UserRepository
import com.cn.langujet.actor.security.response.JwtResponse
import com.cn.langujet.domain.user.model.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoogleAuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val googleAuthClient: GoogleAuthClient,
    @Value("\${google.auth.client.id}") private val googleClientId: String,
    @Value("\${google.auth.client.secret}") private val googleClientSecret: String,
    @Value("\${google.auth.redirect.uri}") private val googleRedirectUri: String
) {
    
    fun authenticateWithGoogle(authCode: String): JwtResponse {
        val tokenResponse = getGoogleAccessToken(authCode)
        val email = googleAuthClient.getGoogleUserInfo(tokenResponse.accessToken).email.toStandardMail()
        val user = userRepository.findByStandardEmailAndDeleted(email).orElseGet {
            registerNewGoogleUser(email)
        }
        val jwt = jwtService.generateTokenFromUserId(user.id ?: "")
        return JwtResponse(jwt, "", user.email)
    }
    
    private fun getGoogleAccessToken(authCode: String): GoogleTokenResponse {
        return googleAuthClient.getGoogleAccessToken(
            authCode = authCode,
            clientId = googleClientId,
            clientSecret = googleClientSecret,
            redirectUri = googleRedirectUri
        )
    }
    
    private fun registerNewGoogleUser(email: String): UserEntity {
        val user = UserEntity(
            id = null,
            standardEmail = email,
            email = email,
            emailVerified = true,
            password = null,
            roles = mutableSetOf(Role.ROLE_STUDENT)
        )
        return userRepository.save(user)
    }
}