package com.cn.speaktest.advice

import org.springframework.http.HttpStatus

open class HttpException(val httpStatus: HttpStatus, override val message: String?) : RuntimeException(message)

class RefreshTokenException(message: String?) : HttpException(HttpStatus.FORBIDDEN, message)

//@Todo use this for send state of user token
class InvalidTokenException(message: String?) : HttpException(HttpStatus.UNAUTHORIZED, message)

class InvalidInputException(message: String?) : HttpException(HttpStatus.BAD_REQUEST, message)

class NotFoundException(message: String?) : HttpException(HttpStatus.NOT_FOUND, message)

class MethodNotAllowedException(message: String?) : HttpException(HttpStatus.METHOD_NOT_ALLOWED, message)