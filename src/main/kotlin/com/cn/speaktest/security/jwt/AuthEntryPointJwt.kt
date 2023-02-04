package com.cn.speaktest.security.jwt

import com.cn.speaktest.advice.Message
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.print(
            Message(
                HttpStatus.UNAUTHORIZED,
                authException.message,
                authException.stackTraceToString()
            ).toJson()
        )
    }
}

private fun Message.toJson(): String {
    return "{\n" +
            "    \"status\": \"$status\",\n" +
            "    \"code\": $code,\n" +
            "    \"message\": \"$message\",\n" +
            "    \"data\": $data\n" +
            "}"
}
