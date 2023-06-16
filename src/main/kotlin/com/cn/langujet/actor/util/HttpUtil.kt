package com.cn.langujet.actor.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> toOkResponseEntity(body: T): ResponseEntity<T> = ResponseEntity(body, HttpStatus.OK)
