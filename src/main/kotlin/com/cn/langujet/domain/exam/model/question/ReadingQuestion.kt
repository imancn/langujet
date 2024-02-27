package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

class ReadingTextCompletion(
    index: Int,
    header: String,
    var text: String
) : Question(
    index, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

class ReadingTableCompletion(
    index: Int,
    header: String,
    var table: List<List<String?>>,
) : Question(
    index, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

class ReadingMultipleChoice(
    index: Int,
    header: String,
    var selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    index, header,
    QuestionType.READING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
)

class ReadingMatchingFeatures(
    index: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    index, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

class ReadingMatchingEndings(
    index: Int,
    header: String,
    var startingPhrases: List<String>,
    var endingPhrases: List<String>,
) : Question(
    index, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES
)

class ReadingMatchingHeadings(
    index: Int,
    header: String,
    var headings: List<String>
) : Question(
    index, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES
)

class ReadingTrueFalse(
    index: Int,
    header: String,
    var issues: List<String>,
) : Question(
    index, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE
)

class ReadingSelectiveTextCompletion(
    index: Int,
    header: String,
    var text: String,
    var items: List<String>
) : Question(index, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES)

class ReadingFlowChartCompletion(
    index: Int,
    header: String,
    var content: String,
    var issues: List<String>,
) : Question(index, header, QuestionType.READING_FLOWCHART_COMPLETION, AnswerType.TEXT_ISSUES)