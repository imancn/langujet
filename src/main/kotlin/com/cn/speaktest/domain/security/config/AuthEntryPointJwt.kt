package com.cn.speaktest.application.security.security.config

import com.cn.speaktest.application.advice.InvalidTokenException
import com.cn.speaktest.application.advice.Message
import com.cn.speaktest.application.security.security.services.JwtService
import com.cn.speaktest.application.security.security.services.UserDetailsServiceImpl
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPointJwt(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl
) : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        try {
            when (authException) {
                is BadCredentialsException -> throw authException

                is InsufficientAuthenticationException -> {
                    val jwt = jwtService.parseJwt(request)
                    val userId = jwtService.getUserIdFromJwtToken(jwt)

                    if (!userDetailsService.userExist(userId))
                        throw InvalidTokenException("User Not Found")
                }

                else -> throw authException
            }
        } catch (exception: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.print(
                Message(
                    HttpStatus.UNAUTHORIZED,
                    exception.message,
                    exception.stackTraceToString()
                ).toJson()
            )
        }

    }
}