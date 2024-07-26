package com.cn.langujet.application.advice

import com.rollbar.notifier.Rollbar
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdvice(private val rollbar: Rollbar) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    @ExceptionHandler(value = [HttpException::class])
    fun handleHttpException(ex: HttpException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString())
        return ResponseEntity(
            ErrorMessageResponse("${ex.message}"),
            ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [LogicalException::class])
    fun handleLogicalException(ex: LogicalException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString(), ex.message)
        return ResponseEntity(
            ErrorMessageResponse("${ex.message}"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
    
    @ExceptionHandler(value = [UnprocessableException::class])
    fun handleHttpException(ex: UnprocessableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return ResponseEntity(ErrorMessageResponse("${ex.message}"), ex.httpStatus)
    }
    
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity(
            ex.detailMessageArguments.toString(),
            HttpStatus.BAD_REQUEST
        )
    }
    
    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity(
            ex.message,
            HttpStatus.BAD_REQUEST
        )
    }
    
    // Todo: Remove after test
    @ExceptionHandler(value = [HttpMessageConversionException::class])
    fun handleHttpMessageConversionException(ex: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity(
            ex.message,
            HttpStatus.BAD_REQUEST
        )
    }
}