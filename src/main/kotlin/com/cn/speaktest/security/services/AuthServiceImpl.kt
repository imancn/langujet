package com.cn.speaktest.security.services

import com.cn.speaktest.security.api.AuthService
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val jwtService: JwtService
): AuthService {

    override fun getUserIdFromAuthorizationHeader(authorizationHeader: String?): String {
        return jwtService.getUserIdFromJwtToken(
            jwtService.parseAuthorizationHeader(authorizationHeader)
        )
    }
}