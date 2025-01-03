package com.cn.langujet.domain.answer.model

enum class TrueFalseAnswerType {
    TRUE,
    FALSE,
    NOT_GIVEN,
    YES,
    NO;
    
    fun title(): String {
        return this.name
            .replace("Listening_", "")
            .replace("READING_", "")
            .lowercase()
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}
