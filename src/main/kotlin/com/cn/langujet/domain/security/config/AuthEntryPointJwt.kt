package com.cn.langujet.domain.security.config

import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.domain.security.services.JwtService
import com.cn.langujet.domain.security.services.UserDetailsServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPointJwt(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl,
    private val modelMapper: ObjectMapper
) : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException
    ) {
        try {
            when (authException) {
                is BadCredentialsException -> throw authException

                is InsufficientAuthenticationException -> {
                    val jwt = jwtService.parseJwt(request)
                    val userId = jwtService.getUserIdFromJwtToken(jwt)

                    if (!userDetailsService.userExist(userId)) throw InvalidTokenException("User Not Found")
                }

                else -> throw authException
            }
        } catch (exception: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.print(
                modelMapper.writeValueAsString(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.message)
                )
            )
        }

    }
}
