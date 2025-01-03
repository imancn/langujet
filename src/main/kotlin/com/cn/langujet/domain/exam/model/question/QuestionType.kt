package com.cn.langujet.domain.exam.model.question

enum class QuestionType {
    WRITING,
    SPEAKING,

    READING_TEXT_COMPLETION,
    READING_MULTIPLE_CHOICES,
    READING_TRUE_FALSE,
    READING_MATCHING_ENDINGS,
    READING_MATCHING_HEADINGS,
    READING_MATCHING_FEATURES,
    READING_TABLE_COMPLETION,
    READING_SELECTIVE_TEXT_COMPLETION,
    READING_FLOWCHART_COMPLETION,

    LISTENING_TABLE_COMPLETION,
    LISTENING_MATCHING_FEATURES,
    LISTENING_LABELLING,
    LISTENING_TEXT_COMPLETION,
    LISTENING_MULTIPLE_CHOICES,
    LISTENING_PHOTO_COMPLETION,
    LISTENING_SELECTIVE_PHOTO_COMPLETION;
    
    fun title(): String {
        return this.name
            .replace("LISTENING_", "")
            .replace("READING_", "")
            .lowercase()
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}