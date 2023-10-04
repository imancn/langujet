package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

class ReadingTextCompletion(
    index: Int,
    time: Long,
    header: String,
    var text: String
) : Question(
    index, time, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

class ReadingTableCompletion(
    index: Int,
    time: Long,
    header: String,
    var table: List<List<String?>>,
) : Question(
    index, time, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

class ReadingMultipleChoice(
    index: Int,
    time: Long,
    header: String,
    selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    index, time, header,
    QuestionType.READING_CHOICES,
    run { if (selectNum > 1) AnswerType.TEXT else AnswerType.TEXT_ISSUES }
)

class ReadingMatchingFeatures(
    index: Int,
    time: Long,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    index, time, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

class ReadingMatchingEndings(
    index: Int,
    time: Long,
    header: String,
    var startingPhrases: List<String>,
    var endingPhrases: List<String>,
) : Question(
    index, time, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES
)

class ReadingMatchingHeadings(
    index: Int,
    time: Long,
    header: String,
    var headings: List<String>
) : Question(
    index, time, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES
)

class ReadingTrueFalse(
    index: Int,
    time: Long,
    header: String,
    var issues: List<String>,
) : Question(
    index, time, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE
)

class ReadingSelectiveTextCompletion(
    index: Int,
    time: Long,
    header: String,
    var text: String,
    var items: List<String>
) : Question(index, time, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES)