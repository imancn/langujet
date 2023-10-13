package com.cn.langujet.domain.exam.model.question

enum class QuestionType {
    WRITING,
    SPEAKING,

    READING_TEXT_COMPLETION,
    READING_CHOICES,
    READING_TRUE_FALSE,
    READING_MATCHING_ENDINGS,
    READING_MATCHING_HEADINGS,
    READING_MATCHING_FEATURES,
    READING_TABLE_COMPLETION,
    READING_SELECTIVE_TEXT_COMPLETION,

    LISTENING_TABLE_COMPLETION,
    LISTENING_MATCHING_FEATURES,
    LISTENING_LABELLING,
    LISTENING_TEXT_COMPLETION,
    LISTENING_MULTIPLE_CHOICE,
}