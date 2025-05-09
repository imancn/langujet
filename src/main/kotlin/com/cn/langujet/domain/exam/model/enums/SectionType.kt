package com.cn.langujet.domain.exam.model.enums

enum class SectionType {
    READING,
    SPEAKING,
    LISTENING,
    WRITING;
    
    fun title(): String {
        return this.name
            .lowercase()
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}