package com.cn.langujet.application.arch.advice

import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse("invalid.input", ex.detailMessageArguments.toString()),
            HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse("invalid.input", ex.message ?: ""),
            HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [NoSuchElementException::class])
    fun handleNoSuchElementException(
        ex: NoSuchElementException,
        request: WebRequest
    ): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse("not.found", ex.message ?: ""),
            HttpStatus.NOT_FOUND
        )
    }
    
    // Todo: Remove after test
    @ExceptionHandler(value = [HttpMessageConversionException::class])
    fun handleHttpMessageConversionException(ex: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(
            ErrorMessageResponse("invalid.input", ex.message ?: ""),
            HttpStatus.BAD_REQUEST
        )
    }
}