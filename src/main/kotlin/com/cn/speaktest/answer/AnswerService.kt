package com.cn.speaktest.answer

import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.answer.api.request.ChoiceAnswerRequest
import com.cn.speaktest.answer.api.request.TextAnswerRequest
import com.cn.speaktest.answer.api.request.TrueFalseAnswerRequest
import com.cn.speaktest.answer.api.request.VoiceAnswerRequest
import com.cn.speaktest.answer.model.Answer
import com.cn.speaktest.exam.service.ExamIssueService
import com.cn.speaktest.exam.service.ExamSessionService
import org.springframework.stereotype.Service

private const val EXAM_SESSION_EXPIRED = "There is no available exam session for this answer"

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val examIssueService: ExamIssueService,
    private val examSessionService: ExamSessionService
){

    fun submitTextAnswer(answerRequest: TextAnswerRequest): Answer.Text {
        val answer = Answer.Text(
            examIssueId = answerRequest.examIssueId,
            userId = answerRequest.userId,
            text = answerRequest.text
        )
        if (examSessionService.isExamIssueAvailable(answerRequest.examIssueId))
            return answerRepository.save(answer).also {
                examIssueService.setAnswer(it.examIssueId, answer)
            }
        else throw MethodNotAllowedException(EXAM_SESSION_EXPIRED)
    }

    fun submitChoiceAnswer(answerRequest: ChoiceAnswerRequest): Answer.Choice {
        val answer = Answer.Choice(
            examIssueId = answerRequest.examIssueId,
            userId = answerRequest.userId,
            choice = answerRequest.choice
        )
        if (examSessionService.isExamIssueAvailable(answerRequest.examIssueId))
            return answerRepository.save(answer).also {
                examIssueService.setAnswer(it.examIssueId, answer)
            }
        else throw MethodNotAllowedException(EXAM_SESSION_EXPIRED)
    }

    fun submitTrueFalseAnswer(answerRequest: TrueFalseAnswerRequest): Answer.TrueFalse {
        val answer = Answer.TrueFalse(
            examIssueId = answerRequest.examIssueId,
            userId = answerRequest.userId,
            isTrue = answerRequest.isTrue
        )
        if (examSessionService.isExamIssueAvailable(answerRequest.examIssueId))
            return answerRepository.save(answer).also {
                examIssueService.setAnswer(it.examIssueId, answer)
            }
        else throw MethodNotAllowedException(EXAM_SESSION_EXPIRED)
    }

    fun submitVoiceAnswer(answerRequest: VoiceAnswerRequest): Answer.Voice {
        val answer = Answer.Voice(
            examIssueId = answerRequest.examIssueId,
            userId = answerRequest.userId,
            audioUrl = answerRequest.audioUrl
        )
        if (examSessionService.isExamIssueAvailable(answerRequest.examIssueId))
            return answerRepository.save(answer).also {
                examIssueService.setAnswer(it.examIssueId, answer)
            }
        else throw MethodNotAllowedException(EXAM_SESSION_EXPIRED)
    }

    fun getAnswersByExamIssueId(examIssueId: String): List<Answer> {
        return answerRepository.findByExamIssueId(examIssueId)
    }

    fun getAnswerById(id: String): Answer {
        return answerRepository.findById(id).orElseThrow {
            NotFoundException("Answer with id: $id not found")
        }
    }

}