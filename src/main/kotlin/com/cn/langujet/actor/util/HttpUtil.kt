package com.cn.langujet.actor.util

import org.springframework.http.ResponseEntity
import java.net.URI

fun <T> toOkResponseEntity(body: T): ResponseEntity<T> = ResponseEntity.ok(body)

fun <T> toCreatedResponseEntity(body: T, uri: URI): ResponseEntity<T> = ResponseEntity.created(uri).body(body)
