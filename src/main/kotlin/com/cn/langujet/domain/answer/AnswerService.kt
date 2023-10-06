package com.cn.langujet.domain.answer

import com.cn.langujet.actor.answer.payload.request.AnswerRequest
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.answer.model.Answer
import com.cn.langujet.domain.exam.service.ExamSessionService
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val examSessionService: ExamSessionService,
    private val studentService: StudentService
) {
    private val EXAM_SESSION_EXPIRED = "There is no available exam session for this answer"
    fun submitAnswer(request: AnswerRequest, token: String): Answer {
        val examSession = examSessionService.getExamSessionById(request.examSessionId)
        if (!studentService.doesStudentOwnsAuthToken(
                token,
                examSession.studentId
            )
        ) throw InvalidTokenException("Exam Session with id: ${request.examSessionId} is not belong to your token")
        return saveAnswer(request.convertToAnswer())
    }

    private fun <T : Answer> saveAnswer(answer: T): T {
        if (examSessionService.isExamAvailable(answer.examSessionId)) {
            return answerRepository.save(answer)
        } else throw MethodNotAllowedException(EXAM_SESSION_EXPIRED)
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