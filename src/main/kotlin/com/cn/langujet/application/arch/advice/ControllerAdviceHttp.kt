package com.cn.langujet.application.arch.advice

import com.cn.langujet.application.arch.log.LoggerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ControllerAdviceHttp(
    @Autowired private val loggerService: LoggerService
) {
    @ExceptionHandler(value = [InvalidCredentialException::class])
    fun handleInvalidTokenException(ex: InvalidCredentialException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        loggerService.error(ex)
        return ResponseEntity(
            ErrorMessageResponse(ex.key, loggerService.error(ex)), ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        loggerService.error(ex)
        return ResponseEntity(
            ErrorMessageResponse(ex.key, loggerService.error(ex)), ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [InvalidInputException::class])
    fun handleInternalServerError(ex: InvalidInputException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        loggerService.error(ex)
        return ResponseEntity(
            ErrorMessageResponse(ex.key, loggerService.error(ex)), ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [InternalServerError::class])
    fun handleInternalServerError(ex: InternalServerError, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        loggerService.error(ex)
        return ResponseEntity(
            ErrorMessageResponse(ex.key, loggerService.error(ex)), ex.httpStatus
        )
    }
    
    @ExceptionHandler(value = [UnprocessableException::class])
    fun handleUnprocessableEntityException(ex: UnprocessableException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {
        loggerService.error(ex)
        return ResponseEntity(
            ErrorMessageResponse(ex.key, loggerService.error(ex)), ex.httpStatus
        )
    }
}