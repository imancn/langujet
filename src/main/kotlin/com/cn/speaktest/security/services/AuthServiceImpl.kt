package com.cn.speaktest.security.services

import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.security.api.AuthService
import com.cn.speaktest.security.model.User
import com.cn.speaktest.security.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : AuthService {

    override fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String {
        return jwtService.getUserIdFromJwtToken(
            jwtService.parseAuthorizationHeader(authorizationHeader)
        )
    }

    override fun getUserById(userId: String): User {
        return userRepository.findById(userId).orElseThrow {
            throw NotFoundException("User not found")
        }
    }

    override fun getUserByAuthToken(auth: String?): User {
        return userRepository.findById(
            getUserIdFromAuthorizationHeader(auth)
        ).orElseThrow { NotFoundException("User Not found") }
    }
}