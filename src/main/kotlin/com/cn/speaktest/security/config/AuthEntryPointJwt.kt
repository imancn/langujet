package com.cn.speaktest.security.config

import com.cn.speaktest.advice.InvalidTokenException
import com.cn.speaktest.advice.Message
import com.cn.speaktest.security.services.JwtService
import com.cn.speaktest.security.services.UserDetailsServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

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
            val jwt = jwtService.parseJwt(request)
            val userId = jwtService.getUserIdFromJwtToken(jwt)

            if (!userDetailsService.userExist(userId))
                throw InvalidTokenException("User Not Found")

            throw authException
        } catch (exception: Exception){
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
