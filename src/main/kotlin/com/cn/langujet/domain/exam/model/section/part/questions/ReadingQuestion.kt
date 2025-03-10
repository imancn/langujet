package com.cn.langujet.domain.exam.model.section.part.questions

import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.exam.model.enums.QuestionType
import org.springframework.data.annotation.TypeAlias

@TypeAlias("reading_text_completion_questions")
class ReadingTextCompletion(
    order: Int,
    header: String,
    var text: String
) : Question(
    order, header, QuestionType.READING_TEXT_COMPLETION, AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_table_completion_questions")
class ReadingTableCompletion(
    order: Int,
    header: String,
    var table: List<List<String?>>,
) : Question(
    order, header, QuestionType.READING_TABLE_COMPLETION, AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_multiple_choices_questions")
class ReadingMultipleChoice(
    order: Int,
    header: String,
    var selectNum: Int,
    var issues: List<MultipleChoiceIssue>,
) : Question(
    order, header,
    QuestionType.READING_MULTIPLE_CHOICES,
    AnswerType.MULTIPLE_CHOICE
)

@TypeAlias("reading_matching_features_questions")
class ReadingMatchingFeatures(
    order: Int,
    header: String,
    var itemsHeader: String?,
    var items: List<String>,
    var featuresHeader: String?,
    var features: List<String>
) : Question(
    order, header, QuestionType.READING_MATCHING_FEATURES, AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_matching_endings_questions")
class ReadingMatchingEndings(
    order: Int,
    header: String,
    var startingPhrases: List<String>,
    var endingPhrases: List<String>,
) : Question(
    order, header, QuestionType.READING_MATCHING_ENDINGS, AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_matching_headings_completion_questions")
class ReadingMatchingHeadings(
    order: Int,
    header: String,
    var headings: List<String>
) : Question(
    order, header, QuestionType.READING_MATCHING_HEADINGS, AnswerType.TEXT_ISSUES
)

@TypeAlias("reading_true_false_questions")
class ReadingTrueFalse(
    order: Int,
    header: String,
    var issues: List<String>,
) : Question(
    order, header, QuestionType.READING_TRUE_FALSE, AnswerType.TRUE_FALSE
)

@TypeAlias("reading_selective_text_completion_questions")
class ReadingSelectiveTextCompletion(
    order: Int,
    header: String,
    var text: String,
    var content: String?,
    var items: List<String>
) : Question(order, header, QuestionType.READING_SELECTIVE_TEXT_COMPLETION, AnswerType.TEXT_ISSUES)

@TypeAlias("reading_flowchart_completion_questions")
class ReadingFlowchartCompletion(
    order: Int,
    header: String,
    var content: String,
    var issues: List<String>,
) : Question(order, header, QuestionType.READING_FLOWCHART_COMPLETION, AnswerType.TEXT_ISSUES)