package com.cn.speaktest.advice

import org.springframework.http.HttpStatus

class Message(val status: String, val code: Int, val message: String?, val data: Any?) {

    constructor(status: HttpStatus, message: String?, data: Any?) : this(
        status = status.reasonPhrase,
        code = status.value(),
        message = message,
        data = data
    )

    constructor(data: Any?, message: String? = null) : this(
        status = HttpStatus.OK.name,
        code = HttpStatus.OK.value(),
        message = message,
        data = data
    )

    fun toJson(): String {
        return "{\n" +
                "    \"status\": \"$status\",\n" +
                "    \"code\": $code,\n" +
                "    \"message\": \"$message\",\n" +
                "    \"data\": $data\n" +
                "}"
    }
}

fun Any?.toOkMessage(message: String? = null) = Message(this, message)