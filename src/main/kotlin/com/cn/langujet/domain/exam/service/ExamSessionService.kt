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
    fun enrollExamSession(studentId: String, examVariantId: String): ExamSessionEnrollResponse {
        val examVariant = examVariantService.getExamVariantById(examVariantId)

        val examId = examService.getRandomActiveExamIdByType(examVariant.examType)
        val sections = sectionService.getSectionsByExamId(examId)

        val sectionOrders = sections.filter { examVariant.sectionTypes.contains(it.sectionType) }.map { it.order }.sorted()

        val examSession = examSessionRepository.save(
            ExamSession(
                studentId, examId, examVariant.id ?: "", sectionOrders, Date(System.currentTimeMillis())
            )
        )

        return ExamSessionEnrollResponse(examSession)
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

    fun getExamSection(
        authToken: String, examSessionId: String, sectionOrder: Int
    ): SectionDTO {
        val examSession = getStudentExamSession(authToken, examSessionId)

        checkExamSessionAndSectionAvailability(examSession, sectionOrder)

        val section = try {
            sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionOrder)
        } catch (ex: NotFoundException) {
            throw MethodNotAllowedException("There is no section left")
        }

        if (examSession.state == ExamSessionState.ENROLLED) {
            examSessionRepository.save(examSession.also {
                val loadingDelay = 30_000L
                val now = Date(System.currentTimeMillis())
                val examDuration = (examService.getExamById(examSession.examId).examDuration) * 1000
                it.startDate = now
                it.endDate = Date(now.time + examDuration + loadingDelay)
                it.state = ExamSessionState.STARTED
            })
        }
        return SectionDTO(section).also { it.id = null ; it.examId = null }
    }

    fun finishExamSession(
        authToken: String, examSessionId: String
    ): ExamSessionResponse {
        val examSession = getStudentExamSession(authToken, examSessionId).let {
            if (it.state == ExamSessionState.ENROLLED)
                throw MethodNotAllowedException("The exam session has not been started")
            else if (it.state.order >= ExamSessionState.FINISHED.order)
                throw MethodNotAllowedException("The exam session has been finished")
            else {
                finishExamSession(it)
            }
        }
        return ExamSessionResponse(
            examSession, null,
            examVariantService.getExamVariantById(examSession.examVariantId).correctionType
        )
    }
    
    private fun finishExamSession(examSession: ExamSession): ExamSession {
        examSession.state = ExamSessionState.FINISHED
        examSessionRepository.save(examSession)
        val exam = examService.getExamById(examSession.examId)
        resultService.initiateResult(examSession.id ?: "", exam.type)
        correctionService.makeExamSessionCorrection(examSession)
        return getExamSessionById(examSession.id ?: "")
    }

    fun checkExamSessionAndSectionAvailability(examSession: ExamSession, sectionOrder: Int) {
        if (!examSession.sectionOrders.contains(sectionOrder))
            throw MethodNotAllowedException("You don't have permission to this section")

        if (examSession.state.order >= ExamSessionState.FINISHED.order)
            throw MethodNotAllowedException("The exam session has been finished")

        if ((examSession.endDate?.time ?: (Long.MAX_VALUE)) < System.currentTimeMillis()) {
            finishExamSession(examSession)
            throw MethodNotAllowedException("The exam session time is over")
        }
    }
    
    fun getExamSessionById(id: String): ExamSession {
        return examSessionRepository.findById(id).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
    
    fun finalizeCorrection(examSessionId: String) {
        examSessionRepository.save(
            getExamSessionById(examSessionId).also {
                it.state = ExamSessionState.CORRECTED
                it.correctionDate = Date(System.currentTimeMillis())
            }
        )
    }
}