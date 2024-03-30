package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.actor.util.models.paginate
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.cn.langujet.domain.exam.repository.ExamSessionCustomRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.result.service.ResultService
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class ExamSessionService(
    private val examSessionRepository: ExamSessionRepository,
    private val examSessionCustomRepository: ExamSessionCustomRepository,
    private val examService: ExamService,
    private val examVariantService: ExamVariantService,
    private val sectionService: SectionService,
    private val studentService: StudentService,
    private val correctionService: CorrectionService,
    private val resultService: ResultService
) {
    fun getExamSessionById(id: String): ExamSession {
        return examSessionRepository.findById(id).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
    
    fun getStudentExamSession(
        authToken: String,
        examSessionId: String,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        if (!studentService.doesStudentOwnAuthToken(
                authToken, examSession.studentId
            )
        ) throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return examSession
    }
    
    fun getStudentExamSessionResponse(
        authToken: String,
        examSessionId: String,
    ): ExamSessionResponse {
        val examSession = getStudentExamSession(authToken, examSessionId)
        val exam = ExamDTO(examService.getExamById(examSession.examId))
        val examVariant = examVariantService.getExamVariantById(examSession.examVariantId)
        return ExamSessionResponse(examSession, exam, examVariant.correctionType)
    }
    
    fun searchExamSessions(auth: String, request: ExamSessionSearchRequest): CustomPage<ExamSessionResponse> {
        return examSessionCustomRepository.searchExamSessions(
            request, studentService.getStudentByAuthToken(auth).id ?: ""
        ).paginate(request.pageSize, request.pageNumber)
    }
    
    fun enrollExamSession(studentId: String, examVariantId: String): ExamSessionEnrollResponse {
        val examVariant = examVariantService.getExamVariantById(examVariantId)
        val examId = examService.getRandomActiveExamIdByType(examVariant.examType)
        val sections = sectionService.getSectionsByExamId(examId)
        val sectionOrders = sections.filter {
            examVariant.sectionTypes.contains(it.sectionType)
        }.map { it.order }.sorted()
        val examSession = examSessionRepository.save(
            ExamSession(
                studentId, examId, examVariant.id ?: "", sectionOrders, Date(System.currentTimeMillis())
            )
        )
        return ExamSessionEnrollResponse(examSession)
    }
    
    fun getExamSection(
        authToken: String, examSessionId: String, sectionOrder: Int
    ): SectionDTO {
        val examSession = examSessionRepository.findByStudentIdAndState(
            studentService.getStudentByAuthToken(authToken).id ?: "", ExamSessionState.STARTED
        ).getOrElse {
            getStudentExamSession(authToken, examSessionId)
        }
        if (examSessionId != examSession.id) {
            throw MethodNotAllowedException("You should finish your started exam session")
        }
        if (!examSession.sectionOrders.contains(sectionOrder)) {
            throw MethodNotAllowedException("You don't have permission to this section")
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
    
    fun finishExamSession(authToken: String, examSessionId: String): ExamSessionResponse {
        var examSession = getStudentExamSession(authToken, examSessionId)
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
        return ExamSessionResponse(
            examSession, null, examVariantService.getExamVariantById(examSession.examVariantId).correctionType
        )
    }
    
    fun finalizeCorrection(examSessionId: String) {
        examSessionRepository.save(getExamSessionById(examSessionId).also {
            it.state = ExamSessionState.CORRECTED
            it.correctionDate = Date(System.currentTimeMillis())
        })
    }
}