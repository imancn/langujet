package com.cn.langujet.domain.question.model

import com.cn.langujet.domain.answer.model.AnswerType

class ListeningTextCompletion(
    index: Int,
    time: Long,
    header: String,
    var text: String
) : Question(
    index, time, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

class ListeningTableCompletion(
    index: Int,
    time: Long,
    header: String,
    var tableHeader: String,
    var table: List<List<String?>>,
) : Question(
    index, time, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

class ListeningMultipleChoice(
    index: Int,
    time: Long,
    header: String,
    selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    index, time, header,
    QuestionType.LISTENING_MULTIPLE_CHOICE,
    run { if (selectNum > 1) AnswerType.TEXT else AnswerType.TEXT_ISSUES }
)

class ListeningMatchingFeatures(
    index: Int,
    time: Long,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    index, time, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

class ListeningLabelling(
    index: Int,
    time: Long,
    header: String,
    var content: String,
    var labels: List<String>,
    var issues: List<String>,
) : Question(
    index, time, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES
)