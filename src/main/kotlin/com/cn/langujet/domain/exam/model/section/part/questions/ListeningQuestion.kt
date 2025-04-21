package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias("listening_text_completion_questions")
@Document(collection = "questions")
class ListeningTextCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var text: String
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_TEXT_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_table_completion_questions")
@Document(collection = "questions")
class ListeningTableCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var tableHeader: String,
    var table: List<List<String?>>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_TABLE_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_multiple_choice_questions")
@Document(collection = "questions")
class ListeningMultipleChoice(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_MULTIPLE_CHOICES,
    answerType = AnswerType.MULTIPLE_CHOICE
)

@TypeAlias("listening_matching_features_questions")
@Document(collection = "questions")
class ListeningMatchingFeatures(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_MATCHING_FEATURES,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_labelling_questions")
@Document(collection = "questions")
class ListeningLabelling(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var content: String,
    var labels: List<String>,
    var issues: List<String>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_LABELLING,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_photo_completion_questions")
@Document(collection = "questions")
class ListeningPhotoCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var content: String,
    var issues: List<String>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_PHOTO_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("listening_selective_photo_completion")
@Document(collection = "questions")
class ListeningSelectivePhotoCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var content: String?,
    var items: List<String>,
    var issues: List<String>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.LISTENING_SELECTIVE_PHOTO_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)
