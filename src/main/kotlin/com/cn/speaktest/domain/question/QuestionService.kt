package com.cn.speaktest.domain.question

import com.cn.speaktest.actor.question.payload.request.*
import com.cn.speaktest.application.advice.InvalidInputException
import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.answer.model.AnswerType
import com.cn.speaktest.domain.exam.service.ExamService
import com.cn.speaktest.domain.question.model.Question
import com.cn.speaktest.domain.question.model.QuestionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

private const val EXAM_ID_MUST_NOT_BE_NULL = "questionRequest.examId must not be null"

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val examService: ExamService
) {

    fun getQuestionById(id: String): Question? {
        return questionRepository.findById(id).orElseThrow {
            throw NotFoundException("Question with id: $id not found")
        }
    }

    fun getAllQuestionsByFilters(
        id: String?,
        examId: String?,
        topic: String?,
        section: Int?,
        order: Int?,
        answerType: AnswerType?,
        questionType: QuestionType?,
        pageRequest: PageRequest
    ): Page<Question> {
        return questionRepository.findAllQuestionsByFilters(
            id,
            examId,
            topic,
            section,
            order,
            answerType,
            questionType,
            pageRequest
        )
    }

    fun getAllQuestions(): List<Question> {
        return questionRepository.findAll()
    }

    fun getAllQuestionsByTopic(topic: String): List<Question> {
        return questionRepository.findAllByTopic(topic)
    }

    fun getAllQuestionsBySection(section: Int): List<Question> {
        return questionRepository.findAllBySection(section)
    }

    fun getAllQuestionsByAnswerType(answerType: AnswerType): List<Question> {
        return questionRepository.findAllByAnswerType(answerType)
    }

    fun getAllQuestionsByQuestionType(questionType: QuestionType): List<Question> {
        return questionRepository.findAllByQuestionType(questionType)
    }

    fun getAllTextQuestion(): List<Question.Text> {
        return getAllQuestionsByQuestionType(QuestionType.TEXT).filterIsInstance(Question.Text::class.java)
    }

    fun getAllMultipleChoiceQuestion(): List<Question.MultipleChoice> {
        return getAllQuestionsByQuestionType(QuestionType.CHOICE).filterIsInstance(Question.MultipleChoice::class.java)
    }

    fun getAllTrueFalseQuestion(): List<Question.TrueFalse> {
        return getAllQuestionsByQuestionType(QuestionType.TRUE_FALSE).filterIsInstance(Question.TrueFalse::class.java)
    }

    fun getAllPhotoQuestion(): List<Question.Photo> {
        return getAllQuestionsByQuestionType(QuestionType.PHOTO).filterIsInstance(Question.Photo::class.java)
    }

    fun getAllVoiceQuestion(): List<Question.Voice> {
        return getAllQuestionsByQuestionType(QuestionType.VOICE).filterIsInstance(Question.Voice::class.java)
    }

    fun getAllVideoQuestion(): List<Question.Video> {
        return getAllQuestionsByQuestionType(QuestionType.VIDEO).filterIsInstance(Question.Video::class.java)
    }

    fun createTextQuestion(questionRequest: TextQuestionRequest): Question.Text {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.Text
    }

    fun createMultipleChoiceQuestion(questionRequest: MultipleChoiceQuestionRequest): Question.MultipleChoice {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.MultipleChoice
    }

    fun createTrueFalseQuestion(questionRequest: TrueFalseQuestionRequest): Question.TrueFalse {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.TrueFalse
    }

    fun createPhotoQuestion(questionRequest: PhotoQuestionRequest): Question.Photo {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.Photo
    }

    fun createVoiceQuestion(questionRequest: VoiceQuestionRequest): Question.Voice {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.Voice
    }

    fun createVideoQuestion(questionRequest: VideoQuestionRequest): Question.Video {
        return questionRepository.save(
            checkNotExist(
                questionRequest.toQuestion(
                    examService.getExamById(
                        questionRequest.examId ?: throw InvalidInputException(EXAM_ID_MUST_NOT_BE_NULL)
                    )
                )
            )
        ) as Question.Video
    }

    private fun checkNotExist(question: Question): Question {
        return if (
            questionRepository.existsByExam_IdAndSectionAndTopicAndOrder(
                question.examInfo.id,
                question.section,
                question.topic,
                question.order
            )
        ) throw InvalidInputException(
            "A question with " +
                    " exam: ${question.examInfo}," +
                    " section: ${question.section}," +
                    " topic: ${question.topic}," +
                    " order: ${question.order}," +
                    "does exist."
        )
        else question
    }

    fun updateTextQuestion(id: String, questionRequest: TextQuestionRequest): Question.Text? {
        return (getQuestionById(id) as Question.Text).also { question ->
            if (question.questionType == QuestionType.TEXT) {
                updateQuestion(question, questionRequest)
                questionRequest.text?.let { question.text = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    fun updateMultipleChoiceQuestion(
        id: String,
        questionRequest: MultipleChoiceQuestionRequest
    ): Question.MultipleChoice? {
        return (getQuestionById(id) as Question.MultipleChoice).also { question ->
            if (question.questionType == QuestionType.CHOICE) {
                updateQuestion(question, questionRequest)
                questionRequest.text?.let { question.text = it }
                questionRequest.choices?.let { question.choices = it }
                questionRequest.correctChoice?.let { question.correctChoice = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    fun updateTrueFalseQuestion(id: String, questionRequest: TrueFalseQuestionRequest): Question.TrueFalse? {
        return (getQuestionById(id) as Question.TrueFalse).also { question ->
            if (question.questionType == QuestionType.TRUE_FALSE) {
                updateQuestion(question, questionRequest)
                questionRequest.text?.let { question.text = it }
                questionRequest.correctAnswer?.let { question.correctAnswer = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    fun updatePhotoQuestion(id: String, questionRequest: PhotoQuestionRequest): Question.Photo? {
        return (getQuestionById(id) as Question.Photo).also { question ->
            if (question.questionType == QuestionType.PHOTO) {
                updateQuestion(question, questionRequest)
                questionRequest.photoUrl?.let { question.photoUrl = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    fun updateVoiceQuestion(id: String, questionRequest: VoiceQuestionRequest): Question.Voice? {
        return (getQuestionById(id) as Question.Voice).also { question ->
            if (question.questionType == QuestionType.VOICE) {
                updateQuestion(question, questionRequest)
                questionRequest.audioUrl?.let { question.audioUrl = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    fun updateVideoQuestion(id: String, questionRequest: VideoQuestionRequest): Question.Video? {
        return (getQuestionById(id) as Question.Video).also { question ->
            if (question.questionType == QuestionType.VIDEO) {
                updateQuestion(question, questionRequest)
                questionRequest.videoUrl?.let { question.videoUrl = it }
                questionRepository.save(question)
            } else {
                throw InvalidInputException("There is type mismatch between id: $id and ${question::class.java.name}")
            }
        }
    }

    private fun updateQuestion(
        question: Question,
        questionRequest: QuestionRequest
    ) {
        questionRequest.examId?.let { question.examInfo = examService.getExamById(it) }
        questionRequest.topic?.let { question.topic = it }
        questionRequest.section?.let { question.section = it }
        questionRequest.order?.let { question.order = it }
        questionRequest.usageNumber?.let { question.usageNumber = it }
        questionRequest.answerType?.let { question.answerType = it }
    }

    fun updateQuestions(questions: List<Question>) {
        questionRepository.saveAll(
            questions.filter { !it.id.isNullOrBlank() }
        )
    }
}