package com.cn.langujet.domain.security.services

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.security.security.model.User
import com.cn.langujet.domain.security.repository.UserRepository
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

    override fun doesUserOwnsAuthToken(authToken: String, id: String?): Boolean {
        return getUserByAuthToken(authToken).id == id
    }
}