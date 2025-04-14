package com.cn.langujet.application.arch.advice

import com.rollbar.notifier.Rollbar
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.text.MessageFormat
import java.util.*

@RestControllerAdvice
class ControllerAdviceHttp(
    private val bundle: ResourceBundle,
    private val rollbar: Rollbar
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    @ExceptionHandler(value = [InvalidCredentialException::class])
    fun handleInvalidTokenException(ex: InvalidCredentialException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex)
    }
    
    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex)
    }
    
    @ExceptionHandler(value = [InvalidInputException::class])
    fun handleInternalServerError(ex: InvalidInputException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex)
    }
    
    @ExceptionHandler(value = [InternalServerError::class])
    fun handleInternalServerError(ex: InternalServerError, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        rollbar.error(ex)
        logger.error(ex.stackTraceToString(), ex.message)
        return getErrorMessageResponse(ex)
    }
    
    @ExceptionHandler(value = [UnprocessableException::class])
    fun handleUnprocessableEntityException(ex: UnprocessableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        return getErrorMessageResponse(ex)
    }
    
    private fun getErrorMessageResponse(ex: HttpException): ResponseEntity<ErrorMessageResponse> {
        val message = try { bundle.getString(ex.key).let { template -> MessageFormat.format(template, *ex.args) } } catch (_: Exception) { ex.key }
        return ResponseEntity(
            ErrorMessageResponse(ex.key, message), ex.httpStatus
        )
    }
}