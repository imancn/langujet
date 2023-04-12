package com.cn.speaktest.question.api

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.answer.model.AnswerType
import com.cn.speaktest.question.QuestionService
import com.cn.speaktest.question.model.QuestionType
import com.cn.speaktest.question.api.request.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/questions")
class QuestionController(private val questionService: QuestionService) {

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getQuestionById(@PathVariable id: String): Message {
        return questionService.getQuestionById(id).toOkMessage()
    }

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
    ): Message {
        return questionService.getAllQuestionsByFilters(
            id,
            examId,
            topic,
            section,
            order,
            answerType,
            questionType,
            PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
        ).toOkMessage()
    }

    @GetMapping("/text")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllTextQuestions(): Message {
        val questions = questionService.getAllTextQuestion()
        return questions.toOkMessage()
    }

    @GetMapping("/multiple-choice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllMultipleChoiceQuestions(): Message {
        return questionService.getAllMultipleChoiceQuestion().toOkMessage()
    }

    @GetMapping("/true-false")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllTrueFalseQuestions(): Message {
        return questionService.getAllTrueFalseQuestion().toOkMessage()
    }

    @GetMapping("/photo")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllPhotoQuestions(): Message {
        return questionService.getAllPhotoQuestion().toOkMessage()
    }

    @GetMapping("/voice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllVoiceQuestions(): Message {
        return questionService.getAllVoiceQuestion().toOkMessage()
    }

    @GetMapping("/video")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun getAllVideoQuestions(): Message {
        return questionService.getAllVideoQuestion().toOkMessage()
    }

    @PostMapping("/text")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createTextQuestion(@RequestBody request: TextQuestionRequest): Message {
        return questionService.createTextQuestion(request).toOkMessage()
    }

    @PostMapping("/multiple-choice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createMultipleChoiceQuestion(@RequestBody request: MultipleChoiceQuestionRequest): Message {
        return questionService.createMultipleChoiceQuestion(request).toOkMessage()
    }

    @PostMapping("/true-false")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createTrueFalseQuestion(@RequestBody request: TrueFalseQuestionRequest): Message {
        return questionService.createTrueFalseQuestion(request).toOkMessage()
    }

    @PostMapping("/photo")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createPhotoQuestion(@RequestBody request: PhotoQuestionRequest): Message {
        return questionService.createPhotoQuestion(request).toOkMessage()
    }

    @PostMapping("/voice")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createVoiceQuestion(@RequestBody request: VoiceQuestionRequest): Message {
        return questionService.createVoiceQuestion(request).toOkMessage()
    }

    @PostMapping("/video")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun createVideoQuestion(@RequestBody request: VideoQuestionRequest): Message {
        return questionService.createVideoQuestion(request).toOkMessage()
    }

    @PutMapping("/text/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateTextQuestion(
        @PathVariable id: String,
        @RequestBody request: TextQuestionRequest
    ): Message {
        return questionService.updateTextQuestion(id, request).toOkMessage()
    }

    @PutMapping("/multiple-choice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateMultipleChoiceQuestion(
        @PathVariable id: String,
        @RequestBody request: MultipleChoiceQuestionRequest
    ): Message {
        return questionService.updateMultipleChoiceQuestion(id, request).toOkMessage()
    }

    @PutMapping("/true-false/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateTrueFalseQuestion(
        @PathVariable id: String,
        @RequestBody request: TrueFalseQuestionRequest
    ): Message {
        return questionService.updateTrueFalseQuestion(id, request).toOkMessage()
    }

    @PutMapping("/photo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updatePhotoQuestion(
        @PathVariable id: String,
        @RequestBody request: PhotoQuestionRequest
    ): Message {
        return questionService.updatePhotoQuestion(id, request).toOkMessage()
    }

    @PutMapping("/voice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateVoiceQuestion(
        @PathVariable id: String,
        @RequestBody request: VoiceQuestionRequest
    ): Message {
        return questionService.updateVoiceQuestion(id, request).toOkMessage()
    }

    @PutMapping("/video/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') ")
    fun updateVideoQuestion(
        @PathVariable id: String,
        @RequestBody request: VideoQuestionRequest
    ): Message {
        return questionService.updateVideoQuestion(id, request).toOkMessage()
    }
}