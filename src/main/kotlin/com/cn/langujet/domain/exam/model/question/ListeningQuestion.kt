package com.cn.langujet.domain.exam.model.question

import com.cn.langujet.domain.answer.model.AnswerType

class ListeningTextCompletion(
    id: Int,
    header: String,
    var text: String
) : Question(
    id, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

class ListeningTableCompletion(
    id: Int,
    header: String,
    var tableHeader: String,
    var table: List<List<String?>>,
) : Question(
    id, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

class ListeningMultipleChoice(
    id: Int,
    header: String,
    var selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    id, header,
    QuestionType.LISTENING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
)

class ListeningMatchingFeatures(
    id: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    id, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

class ListeningLabelling(
    id: Int,
    header: String,
    var content: String,
    var labels: List<String>,
    var issues: List<String>,
) : Question(
    id, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES
)

class ListeningMapCompletion(
    id: Int,
    header: String,
    var content: String,
    var issues: List<String>,
) : Question(
    id, header, QuestionType.LISTENING_MAP_COMPLETION, AnswerType.TEXT_ISSUES
)