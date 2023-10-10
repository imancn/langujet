package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.ResultDto
import com.cn.langujet.application.advice.AccessDeniedException
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.application.security.security.model.Role
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.Result
import com.cn.langujet.domain.exam.model.SectionResult
import com.cn.langujet.domain.exam.repository.ResultRepository
import com.cn.langujet.domain.professor.ProfessorService
import com.cn.langujet.domain.security.services.AuthService
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service

@Service
class ResultService(
    private val resultRepository: ResultRepository,
    private val examSessionService: ExamSessionService,
    private val authService: AuthService,
    private val studentService: StudentService,
    private val professorService: ProfessorService
) {
    fun createResult(
        authToken: String,
        examSessionId: String,
        result: ResultDto,
    ): ResultDto {
        val examSession = examSessionService.getExamSessionById(examSessionId)
        return doWithPreAuth(authToken, examSession) {
            if (!examSession.isStarted)
                throw MethodNotAllowedException("The exam session has not been started")
            else if (!examSession.isFinished)
                throw MethodNotAllowedException("The exam session has not been finished")
            else if (examSession.isRated)
                throw MethodNotAllowedException("The exam session has been rated")
            else
                ResultDto(resultRepository.save(Result(result)))
        }
    }

    private fun getResultById(
        id: String
    ): Result {
        return resultRepository.findById(id)
            .orElseThrow { throw NotFoundException("Suggestion with ID: $id not found") }
    }

    fun getResultsByExamSessionId(
        authToken: String,
        examSessionId: String
    ): ResultDto {
        val studentId = examSessionService.getExamSessionById(examSessionId).studentId
        if (studentService.doesStudentOwnAuthToken(authToken, studentId))
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return resultRepository.findByExamSessionId(examSessionId).orElseThrow {
            NotFoundException("Suggestions for ExamSession with ID: $examSessionId not found")
        }.let { ResultDto(it) }
    }

    fun updateResult(
        auth: String,
        result: ResultDto
    ): ResultDto {
        val examSession = examSessionService.getExamSessionById(result.examSessionId)
        return doWithPreAuth(auth, examSession) {
            resultRepository.save(
                getResultById(result.id!!).also { existingResult ->
                    result.recommendation.let { existingResult.recommendation = it }
                    result.score.let { existingResult.score = it }
                    result.sectionResults.let {
                        existingResult.sectionResults = it.map { sectionResultDto -> SectionResult(sectionResultDto) }
                    }
                }
            ).let { ResultDto(it) }
        }
    }

    fun doWithPreAuth(auth: String, examSession: ExamSession, function: () -> ResultDto): ResultDto {
        val user = authService.getUserByAuthToken(auth)
        val doesProfessorOwnsAuthToken = professorService.doesProfessorOwnAuthToken(auth, examSession.professorId)
        val isAdmin = user.roles.contains(Role.ROLE_ADMIN)
        if (!doesProfessorOwnsAuthToken && !isAdmin)
            throw AccessDeniedException("Exam Session with id: ${examSession.id} is not belong to your token")
        else return function()
    }
}