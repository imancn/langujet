package com.cn.langujet.actor.question.api

import com.cn.langujet.actor.question.payload.request.*
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.answer.model.AnswerType
import com.cn.langujet.domain.question.QuestionService
import com.cn.langujet.domain.question.model.Question
import com.cn.langujet.domain.question.model.QuestionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/questions")
class QuestionController(private val questionService: QuestionService) {

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getQuestionById(
        @PathVariable id: String
    ): ResponseEntity<Question> =
        toOkResponseEntity(questionService.getQuestionById(id))

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllQuestionsByFilters(
        @RequestParam id: String?,
        @RequestParam examId: String?,
        @RequestParam topic: String?,
        @RequestParam section: Int?,
        @RequestParam order: Int?,
        @RequestParam answerType: AnswerType?,
        @RequestParam questionType: QuestionType?,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "id") sortBy: String?
    ): ResponseEntity<Page<Question>> =
        toOkResponseEntity(
            questionService.getAllQuestionsByFilters(
                id,
                examId,
                topic,
                section,
                order,
                answerType,
                questionType,
                PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
            )
        )

    @GetMapping("/text")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllTextQuestions(): ResponseEntity<List<Question>> =
        toOkResponseEntity(questionService.getAllTextQuestion())

    @GetMapping("/multiple-choice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllMultipleChoiceQuestions(): ResponseEntity<List<Question.MultipleChoice>> =
        toOkResponseEntity(questionService.getAllMultipleChoiceQuestion())

    @GetMapping("/true-false")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllTrueFalseQuestions(): ResponseEntity<List<Question.TrueFalse>> =
        toOkResponseEntity(questionService.getAllTrueFalseQuestion())

    @GetMapping("/photo")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllPhotoQuestions(): ResponseEntity<List<Question.Photo>> =
        toOkResponseEntity(questionService.getAllPhotoQuestion())

    @GetMapping("/voice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllVoiceQuestions(): ResponseEntity<List<Question.Voice>> =
        toOkResponseEntity(questionService.getAllVoiceQuestion())

    @GetMapping("/video")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllVideoQuestions(): ResponseEntity<List<Question.Video>> =
        toOkResponseEntity(questionService.getAllVideoQuestion())

    @PostMapping("/text")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createTextQuestion(
        @RequestBody request: TextQuestionRequest
    ): ResponseEntity<Question.Text> =
        toOkResponseEntity(questionService.createTextQuestion(request))

    @PostMapping("/multiple-choice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createMultipleChoiceQuestion(
        @RequestBody request: MultipleChoiceQuestionRequest
    ): ResponseEntity<Question.MultipleChoice> =
        toOkResponseEntity(questionService.createMultipleChoiceQuestion(request))

    @PostMapping("/true-false")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createTrueFalseQuestion(
        @RequestBody request: TrueFalseQuestionRequest
    ): ResponseEntity<Question.TrueFalse> =
        toOkResponseEntity(questionService.createTrueFalseQuestion(request))

    @PostMapping("/photo")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createPhotoQuestion(
        @RequestBody request: PhotoQuestionRequest
    ): ResponseEntity<Question.Photo> = toOkResponseEntity(questionService.createPhotoQuestion(request))

    @PostMapping("/voice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createVoiceQuestion(@RequestBody request: VoiceQuestionRequest): ResponseEntity<Question.Voice> =
        toOkResponseEntity(questionService.createVoiceQuestion(request))

    @PostMapping("/video")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createVideoQuestion(@RequestBody request: VideoQuestionRequest): ResponseEntity<Question.Video> =
        toOkResponseEntity(questionService.createVideoQuestion(request))

    @PutMapping("/text/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateTextQuestion(
        @PathVariable id: String,
        @RequestBody request: TextQuestionRequest
    ): ResponseEntity<Question.Text> =
        toOkResponseEntity(questionService.updateTextQuestion(id, request))

    @PutMapping("/multiple-choice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateMultipleChoiceQuestion(
        @PathVariable id: String,
        @RequestBody request: MultipleChoiceQuestionRequest
    ): ResponseEntity<Question.MultipleChoice> =
        toOkResponseEntity(questionService.updateMultipleChoiceQuestion(id, request))

    @PutMapping("/true-false/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateTrueFalseQuestion(
        @PathVariable id: String,
        @RequestBody request: TrueFalseQuestionRequest
    ): ResponseEntity<Question.TrueFalse> =
        toOkResponseEntity(questionService.updateTrueFalseQuestion(id, request))

    @PutMapping("/photo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updatePhotoQuestion(
        @PathVariable id: String,
        @RequestBody request: PhotoQuestionRequest
    ): ResponseEntity<Question.Photo> =
        toOkResponseEntity(questionService.updatePhotoQuestion(id, request))

    @PutMapping("/voice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateVoiceQuestion(
        @PathVariable id: String,
        @RequestBody request: VoiceQuestionRequest
    ): ResponseEntity<Question.Voice> =
        toOkResponseEntity(questionService.updateVoiceQuestion(id, request))

    @PutMapping("/video/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateVideoQuestion(
        @PathVariable id: String,
        @RequestBody request: VideoQuestionRequest
    ): ResponseEntity<Question.Video> =
        toOkResponseEntity(questionService.updateVideoQuestion(id, request))
}