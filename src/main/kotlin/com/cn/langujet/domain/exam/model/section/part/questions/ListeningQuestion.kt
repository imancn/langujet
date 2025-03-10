package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias

@TypeAlias("listening_text_completion")
class ListeningTextCompletion(
    order: Int,
    header: String,
    var text: String
) : Question(
    order, header, QuestionType.LISTENING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_table_completion")
class ListeningTableCompletion(
    order: Int,
    header: String,
    var tableHeader: String,
    var table: List<List<String?>>,
) : Question(
    order, header, QuestionType.LISTENING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_multiple_choice")
class ListeningMultipleChoice(
    order: Int,
    header: String,
    var selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    order, header,
    QuestionType.LISTENING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
)

@TypeAlias("listening_matching_features")
class ListeningMatchingFeatures(
    order: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    order, header, QuestionType.LISTENING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_labelling")
class ListeningLabelling(
    order: Int,
    header: String,
    var content: String,
    var labels: List<String>,
    var issues: List<String>,
) : Question(
    order, header, QuestionType.LISTENING_LABELLING, AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_photo_completion")
class ListeningPhotoCompletion(
    order: Int,
    header: String,
    var content: String,
    var issues: List<String>,
) : Question(
    order, header, QuestionType.LISTENING_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_selective_photo_completion")
class ListeningSelectivePhotoCompletion(
    order: Int,
    header: String,
    var content: String?,
    var items: List<String>,
    var issues: List<String>,
) : Question(
    order, header, QuestionType.LISTENING_SELECTIVE_PHOTO_COMPLETION, AnswerType.TEXT_ISSUES
)