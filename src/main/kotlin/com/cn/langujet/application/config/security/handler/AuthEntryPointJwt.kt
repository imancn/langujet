package com.cn.langujet.application.config.security.handler

import com.cn.langujet.application.advice.ErrorMessageResponse
import com.cn.langujet.application.advice.InvalidCredentialException
import com.cn.langujet.domain.user.services.JwtService
import com.cn.langujet.domain.user.services.UserDetailsServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class AuthEntryPointJwt(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl,
    private val modelMapper: ObjectMapper,
    private val resourceBundle: ResourceBundle
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
                    
                    if (!userDetailsService.userExist(userId)) throw InvalidCredentialException("User Not Found")
                }
                
                else -> throw authException
            }
        } catch (exception: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            val key = "invalid.credentials"
            response.writer.print(
                modelMapper.writeValueAsString(
                    ErrorMessageResponse(key, resourceBundle.getString(key) ?: "Unexpected error")
                )
            )
        }
        
    }
}
