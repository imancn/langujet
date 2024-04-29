package com.cn.langujet.domain.user.services

import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.domain.user.model.UserDetailsImpl
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
        val roles = userPrincipal.authorities.map { it.authority }
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .claim("roles", roles)
            .claim("email", userPrincipal.email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
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
    
    fun getClaims(token: String): Claims? {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
    }
    
    fun getAuthoritiesFromJwtToken(token: String): List<SimpleGrantedAuthority> {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.get("roles", List::class.java).map {
            SimpleGrantedAuthority(it.toString())
        }
    }
}