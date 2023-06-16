package com.cn.langujet.domain.answer

import com.cn.langujet.actor.answer.payload.request.ChoiceAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TextAnswerRequest
import com.cn.langujet.actor.answer.payload.request.TrueFalseAnswerRequest
import com.cn.langujet.actor.answer.payload.request.VoiceAnswerRequest
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.exam.service.ExamIssueService
import com.cn.langujet.domain.exam.service.ExamSessionService
import org.springframework.stereotype.Service


@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val examIssueService: ExamIssueService,
    private val examSessionService: ExamSessionService
) {
    private val EXAM_SESSION_EXPIRED = "There is no available exam session for this answer"

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