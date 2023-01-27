package com.cn.speaktest.advice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(value = [HttpException::class])
    fun handleTokenRefreshException(ex: HttpException, request: WebRequest): ResponseEntity<Message> {
        return ResponseEntity(
            Message(
                ex.httpStatus,
                ex.message,
                ex.stackTraceToString()
            ),
            ex.httpStatus
        )
    }
}