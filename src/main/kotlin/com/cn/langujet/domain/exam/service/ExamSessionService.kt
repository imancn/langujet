package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.cn.langujet.domain.exam.repository.ExamSessionCustomRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.result.service.ResultService
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.service.ServiceService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExamSessionService(
    private val examSessionRepository: ExamSessionRepository,
    private val examSessionCustomRepository: ExamSessionCustomRepository,
    private val examService: ExamService,
    private val sectionService: SectionService,
    private val correctionService: CorrectionService,
    private val resultService: ResultService,
    private val examGeneratorService: ExamGeneratorService,
    private val serviceService: ServiceService
) {
    fun getExamSessionById(id: String): ExamSession {
        return examSessionRepository.findById(id).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
    
    fun getStudentExamSession(
        examSessionId: String,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        }
        return examSession
    }
    
    fun getStudentExamSessionDetailsResponse(examSessionId: String): ExamSessionDetailsResponse {
        val examSession = getStudentExamSession(examSessionId)
        val sections = sectionService.getSectionsMetaData(examSession.examId)
        return ExamSessionDetailsResponse(sections)
    }
    
    fun searchExamSessions(request: ExamSessionSearchRequest): CustomPage<ExamSessionSearchResponse> {
        return examSessionCustomRepository.searchExamSessions(
            request, Auth.userId()
        )
    }
    
    fun enrollExamSession(userId: String, examServiceId: String): ExamSessionEnrollResponse {
        val examService = serviceService.getById(examServiceId) as ServiceEntity.ExamServiceEntity
        val exam = examGeneratorService.getRandomStudentAvailableExam(userId, examService)
        val examSession = examSessionRepository.save(
            ExamSession(
                userId, exam.id ?: "", exam.type, exam.mode, examService.correctorType, Date(System.currentTimeMillis())
            )
        )
        return ExamSessionEnrollResponse(examSession)
    }
    
    fun getExamSection(
        examSessionId: String, sectionOrder: Int
    ): SectionDTO {
        val examSession = examSessionRepository.findByStudentUserIdAndState(
            Auth.userId(), ExamSessionState.STARTED
        ).getOrElse(0) {
            getStudentExamSession(examSessionId)
        }
        if (examSessionId != examSession.id) {
            throw MethodNotAllowedException("You should finish your started exam session")
        }
        if (examSession.state.order >= ExamSessionState.FINISHED.order) {
            throw MethodNotAllowedException("The exam session has been finished")
        }
        val section = try {
            sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionOrder)
        } catch (ex: NotFoundException) {
            throw MethodNotAllowedException("There is no section left")
        }
        if (examSession.state == ExamSessionState.ENROLLED) {
            examSessionRepository.save(examSession.also {
                it.startDate = Date(System.currentTimeMillis())
                it.state = ExamSessionState.STARTED
            })
        }
        return SectionDTO(section).also { it.id = null; it.examId = null }
    }
    
    fun finishExamSession(examSessionId: String): ExamSessionFinishResponse {
        var examSession = getStudentExamSession(examSessionId)
        if (examSession.state == ExamSessionState.ENROLLED) {
            throw MethodNotAllowedException("The exam session has not been started")
        }
        if (examSession.state.order >= ExamSessionState.FINISHED.order) {
            throw MethodNotAllowedException("The exam session has been finished")
        }
        examSession = examSessionRepository.save(examSession.also {
            it.state = ExamSessionState.FINISHED
            it.endDate = Date(System.currentTimeMillis())
        })
        val exam = examService.getExamById(examSession.examId)
        resultService.initiateResult(examSession.id ?: "", exam.type)
        correctionService.makeExamSessionCorrection(examSession)
        examSession = getExamSessionById(examSession.id ?: "")
        return ExamSessionFinishResponse(examSession.state)
    }
    
    fun finalizeCorrection(examSessionId: String) {
        examSessionRepository.save(getExamSessionById(examSessionId).also {
            it.state = ExamSessionState.CORRECTED
            it.correctionDate = Date(System.currentTimeMillis())
        })
    }
}