package com.cn.speaktest.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import javax.validation.ValidationException

@RestControllerAdvice
class ControllerAdvice {
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
                HttpStatus.NOT_ACCEPTABLE,
                ex.message,
                ex.stackTraceToString()
            ),
            HttpStatus.NOT_ACCEPTABLE
        )
    }

    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<Message> {
        return ResponseEntity(
            Message(
                HttpStatus.NOT_ACCEPTABLE,
                ex.message,
                ex.stackTraceToString()
            ),
            HttpStatus.NOT_ACCEPTABLE
        )
    }
}