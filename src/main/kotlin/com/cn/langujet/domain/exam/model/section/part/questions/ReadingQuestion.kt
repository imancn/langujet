package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@TypeAlias("reading_text_completion_questions")
@Document(collection = "questions")
class ReadingTextCompletion(
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
    questionType = QuestionType.READING_TEXT_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_table_completion_questions")
@Document(collection = "questions")
class ReadingTableCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var table: List<List<String?>>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.READING_TABLE_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_multiple_choices_questions")
@Document(collection = "questions")
class ReadingMultipleChoice(
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
    questionType = QuestionType.READING_MULTIPLE_CHOICES,
    answerType = AnswerType.MULTIPLE_CHOICE
)

@TypeAlias("reading_matching_features_questions")
@Document(collection = "questions")
class ReadingMatchingFeatures(
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
    questionType = QuestionType.READING_MATCHING_FEATURES,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_matching_endings_questions")
@Document(collection = "questions")
class ReadingMatchingEndings(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var startingPhrases: List<String>,
    var endingPhrases: List<String>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.READING_MATCHING_ENDINGS,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_matching_headings_completion_questions")
@Document(collection = "questions")
class ReadingMatchingHeadings(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var headings: List<String>
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.READING_MATCHING_HEADINGS,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_true_false_questions")
@Document(collection = "questions")
class ReadingTrueFalse(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var issues: List<String>,
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.READING_TRUE_FALSE,
    answerType = AnswerType.TRUE_FALSE
)

@TypeAlias("reading_selective_text_completion_questions")
@Document(collection = "questions")
class ReadingSelectiveTextCompletion(
    id: Long?,
    examId: Long,
    sectionId: Long,
    partId: Long,
    order: Int,
    header: String,
    var text: String,
    var content: String?,
    var items: List<String>
) : QuestionEntity(
    id = id,
    examId = examId,
    sectionId = sectionId,
    partId = partId,
    order = order,
    header = header,
    questionType = QuestionType.READING_SELECTIVE_TEXT_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_flowchart_completion_questions")
@Document(collection = "questions")
class ReadingFlowchartCompletion(
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
    questionType = QuestionType.READING_FLOWCHART_COMPLETION,
    answerType = AnswerType.TEXT_ISSUES
)
