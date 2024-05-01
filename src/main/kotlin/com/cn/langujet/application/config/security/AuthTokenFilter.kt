package com.cn.langujet.application.config.security

import com.cn.langujet.domain.user.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class AuthTokenFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        try {
            val jwt = jwtService.parseJwt(request)
            val userId = jwtService.getUserIdFromJwtToken(jwt)
            val authentication = UsernamePasswordAuthenticationToken(
                userId, jwtService.getClaims(jwt), jwtService.getAuthoritiesFromJwtToken(jwt)
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            SecurityContextHolder.getContext().authentication = null
        }
        filterChain.doFilter(request, response)
    }
}