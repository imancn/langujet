package com.cn.langujet.application.advice

import org.springframework.http.HttpStatus
import java.util.*

class Message(
    val description: String,
    val status: Int,
    val message: String?,
    val data: Any?,
    val timestamp: Date
) {
    constructor(status: HttpStatus, message: String?, data: Any?) : this(
        description = status.reasonPhrase,
        status = status.value(),
        message = message,
        data = data,
        timestamp = Date(System.currentTimeMillis())
    )

    constructor(data: Any?, message: String? = null) : this(
        description = HttpStatus.OK.reasonPhrase,
        status = HttpStatus.OK.value(),
        message = message,
        data = data,
        timestamp = Date(System.currentTimeMillis())
    )
}

fun Any?.toOkMessage(message: String? = null) = Message(this, message)