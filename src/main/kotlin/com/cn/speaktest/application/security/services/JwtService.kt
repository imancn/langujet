package com.cn.speaktest.application.security.security.services

import com.cn.speaktest.application.advice.InvalidTokenException
import com.cn.speaktest.application.security.security.model.UserDetailsImpl
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtService {
    @Value("\${app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun generateTokenFromUserId(userId: String?): String {
        return Jwts.builder().setSubject(userId).setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserIdFromJwtToken(token: String?): String {
        return try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
        } catch (e: SignatureException) {
            throw InvalidTokenException("Authorization JWT token is invalid: Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Authorization JWT token is invalid: Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("Authorization JWT token is invalid: JWT token is expired")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("Authorization JWT token is invalid: JWT token is unsupported")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("Authorization JWT token is invalid: JWT claims string is empty")
        } catch (ex: Exception) {
            throw InvalidTokenException("Authorization JWT token is invalid")
        }
    }

    fun parseJwt(request: HttpServletRequest): String {
        return parseAuthorizationHeader(request.getHeader("Authorization"))
    }

    fun parseAuthorizationHeader(authorizationHeader: String?): String {
        if (authorizationHeader.isNullOrEmpty())
            throw InvalidTokenException("Authorization header is missing")
        if (!authorizationHeader.startsWith("Bearer "))
            throw InvalidTokenException("Authorization header is invalid")

        return authorizationHeader.substring(7, authorizationHeader.length)
    }
}