package com.cn.langujet.application.advice

import org.springframework.http.HttpStatus

open class HttpException(val httpStatus: HttpStatus, override val message: String?) : RuntimeException(message)

class RefreshTokenException(message: String?) : HttpException(HttpStatus.UNAUTHORIZED, message)

class InvalidTokenException(message: String?) : HttpException(HttpStatus.UNAUTHORIZED, message)

class AccessDeniedException(message: String?) : HttpException(HttpStatus.FORBIDDEN, message)

class InvalidInputException(message: String?) : HttpException(HttpStatus.NOT_ACCEPTABLE, message)

class NotFoundException(message: String?) : HttpException(HttpStatus.NOT_FOUND, message)

class MethodNotAllowedException(message: String?) : HttpException(HttpStatus.METHOD_NOT_ALLOWED, message)

class EmailNotSentException(message: String?) : HttpException(HttpStatus.INTERNAL_SERVER_ERROR, message)

class FileException(message: String?) : HttpException(HttpStatus.INTERNAL_SERVER_ERROR, message)

class LogicalException(message: String?): RuntimeException(message)