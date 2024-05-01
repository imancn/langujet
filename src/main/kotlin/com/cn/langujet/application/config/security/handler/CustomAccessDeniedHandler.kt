package com.cn.langujet.application.config.security.handler

import com.cn.langujet.application.advice.ErrorMessageResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val modelMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        response.writer.print(modelMapper.writeValueAsString(ErrorMessageResponse(accessDeniedException.message ?: "")))
    }
}