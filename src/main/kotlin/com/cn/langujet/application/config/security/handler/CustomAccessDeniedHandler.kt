package com.cn.langujet.application.config.security.handler

import com.cn.langujet.application.advice.ErrorMessageResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomAccessDeniedHandler(
    private val modelMapper: ObjectMapper,
    private val resourceBundle: ResourceBundle
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        val key = "access.denied"
        response.writer.print(modelMapper.writeValueAsString(ErrorMessageResponse(key, resourceBundle.getString(key) ?: "")))
    }
}