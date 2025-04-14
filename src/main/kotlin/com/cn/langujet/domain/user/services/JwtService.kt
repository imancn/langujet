package com.cn.langujet.domain.user.services

import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.user.model.Role
import com.cn.langujet.domain.user.model.UserDetailsImpl
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtService(
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    @Value("\${app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    fun generateJwtToken(userPrincipal: UserDetailsImpl): String {
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
    
    fun generateAiToken(correctorType: CorrectorType, code: Int): String {
        if (!arrayOf(CorrectorType.AI, CorrectorType.AI_PRO).contains(correctorType)) {
            throw UnprocessableException("AI Token cannot be generated for $correctorType correction type")
        }
        return Jwts.builder()
            .setSubject("$correctorType:$code")
            .claim("roles", arrayOf(Role.ROLE_CORRECTOR_AI))
            .claim("email", "langujet@gmail.com")
            .claim("code", code)
            .claim("type", correctorType.name)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + 315569520000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserIdFromJwtToken(token: String?): String {
        return try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
        } catch (e: SignatureException) {
            throw InvalidCredentialException("Authorization JWT token is invalid: Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            throw InvalidCredentialException("Authorization JWT token is invalid: Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            throw InvalidCredentialException("Authorization JWT token is invalid: JWT token is expired")
        } catch (e: UnsupportedJwtException) {
            throw InvalidCredentialException("Authorization JWT token is invalid: JWT token is unsupported")
        } catch (e: IllegalArgumentException) {
            throw InvalidCredentialException("Authorization JWT token is invalid: JWT claims string is empty")
        } catch (ex: Exception) {
            throw InvalidCredentialException("Authorization JWT token is invalid")
        }
    }

    fun parseJwt(request: HttpServletRequest): String {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader.isNullOrEmpty())
            throw InvalidCredentialException("Authorization header is missing")
        if (!authorizationHeader.startsWith("Bearer "))
            throw InvalidCredentialException("Authorization header is invalid")
        
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
    
    fun generateTokenFromUserId(userId: String): String {
        return generateJwtToken(userDetailsServiceImpl.loadUserByUsername(userId))
    }
}