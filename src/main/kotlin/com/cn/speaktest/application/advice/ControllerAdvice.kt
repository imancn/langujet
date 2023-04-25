package com.cn.speaktest.application.advice

import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    @ExceptionHandler(value = [HttpException::class])
    fun handleHttpException(ex: HttpException, request: WebRequest): ResponseEntity<Message> {
        return ResponseEntity(
            Message(
                ex.httpStatus,
                ex.message,
                ex.stackTraceToString()
            ),
            ex.httpStatus
        )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<Message> {
        return ResponseEntity(
            Message(
                HttpStatus.BAD_REQUEST,
                "MethodArgumentNotValidException",
                ex.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<Message> {
        return ResponseEntity(
            Message(
                HttpStatus.BAD_REQUEST,
                "ValidationException",
                ex.message,
            ),
            HttpStatus.BAD_REQUEST
        )
    }
}