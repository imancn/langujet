package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.models.CustomPage
import com.cn.langujet.application.advice.InvalidCredentialException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.service.smtp.MailSenderService
import com.cn.langujet.domain.correction.service.CorrectionService
import com.cn.langujet.domain.exam.model.ExamSessionEntity
import com.cn.langujet.domain.exam.model.enums.ExamSessionState
import com.cn.langujet.domain.exam.repository.ExamSessionCustomRepository
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.service.ServiceService
import com.cn.langujet.domain.student.service.StudentService
import com.cn.langujet.domain.user.services.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExamSessionService(
    private val examSessionRepository: ExamSessionRepository,
    private val examSessionCustomRepository: ExamSessionCustomRepository,
    private val examService: ExamService,
    private val sectionService: SectionService,
    private val correctionService: CorrectionService,
    private val examGeneratorService: ExamGeneratorService,
    private val serviceService: ServiceService,
    private val userService: UserService,
    private val mailSenderService: MailSenderService,
    private val studentService: StudentService
) {
    fun getExamSessionById(id: String): ExamSessionEntity {
        return examSessionRepository.findById(id).orElseThrow {
            UnprocessableException("ExamSession with id: $id not found")
        }
    }
    
    fun getStudentExamSession(
        examSessionId: String,
    ): ExamSessionEntity {
        val examSession = getExamSessionById(examSessionId)
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidCredentialException("Exam Session with id: $examSessionId is not belong to your token")
        }
        return examSession
    }
    
    fun getStudentExamSessionDetailsResponse(examSessionId: String): ExamSessionDetailsResponse {
        val examSession = getStudentExamSession(examSessionId)
        val sections = sectionService.getSectionsMetaData(examSession.examId).sortedBy { it.order }
        return ExamSessionDetailsResponse(sections)
    }
    
    fun searchExamSessions(request: ExamSessionSearchStudentRequest): CustomPage<ExamSessionSearchResponse> {
        return examSessionCustomRepository.searchExamSessions(
            request, Auth.userId()
        )
    }
    
    fun searchExamSessions(request: ExamSessionSearchAdminRequest): CustomPage<ExamSessionSearchResponse> {
        return examSessionCustomRepository.searchExamSessions(
            ExamSessionSearchStudentRequest(
                states = request.states,
                examTypes = request.examTypes,
                examName = request.examName,
                correctorTypes = request.correctorTypes,
                startDateInterval = request.startDateInterval,
                correctionDateInterval = request.correctionDateInterval,
                pageSize = request.pageSize,
                pageNumber = request.pageNumber,
            ),
            userService.getUserByEmail(request.studentEmail).id ?: ""
        )
    }
    
    
    fun enrollExamSessionByEmail(email: String, examServiceId: String, examId: String? = null): ExamSessionEnrollResponse {
        val userId = userService.getUserByEmail(email).id ?: throw UnprocessableException("User not found")
        return enrollExamSession(userId, examServiceId, examId)
    }
    
    fun enrollExamSession(userId: String, examServiceId: String, examId: String? = null): ExamSessionEnrollResponse {
        val service = serviceService.getById(examServiceId) as ServiceEntity.ExamServiceEntity
        val exam = if (examId != null) {
            examService.getExamById(examId).let {
                if (service.examMode != it.mode || service.examType != it.type) {
                    throw UnprocessableException("Exam and Exam Service are not compatible")
                } else {
                    it
                }
            }
        } else {
            examGeneratorService.getRandomStudentAvailableExam(userId, service)
        }
        val examSession = examSessionRepository.save(
            ExamSessionEntity(
                userId, exam.id ?: "", exam.type, exam.mode, service.correctorType, Date(System.currentTimeMillis())
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
            throw UnprocessableException("You should finish your started exam session")
        }
        if (examSession.state.order >= ExamSessionState.FINISHED.order) {
            throw UnprocessableException("The exam session has been finished")
        }
        val section = try {
            sectionService.getSectionByExamIdAndOrder(examSession.examId, sectionOrder)
        } catch (ex: UnprocessableException) {
            throw UnprocessableException("There is no section left")
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
            throw UnprocessableException("The exam session has not been started")
        }
        if (examSession.state.order >= ExamSessionState.FINISHED.order) {
            return ExamSessionFinishResponse(examSession.state)
        }
        examSession = examSessionRepository.save(examSession.also {
            it.state = ExamSessionState.FINISHED
            it.endDate = Date(System.currentTimeMillis())
        })
        correctionService.initiateExamSessionCorrection(examSession)
        examSession = getExamSessionById(examSession.id ?: "")
        return ExamSessionFinishResponse(examSession.state)
    }
    
    fun finalizeCorrection(examSessionId: String) {
        val examSession = getExamSessionById(examSessionId)
        if (examSession.state == ExamSessionState.FINISHED) {
            examSession.state = ExamSessionState.CORRECTED
            examSession.correctionDate = Date(System.currentTimeMillis())
            val exam = examService.getExamById(examSession.examId)
            examSessionRepository.save(examSession).run {
                val student = studentService.getStudentByUserId(examSession.studentUserId)
                mailSenderService.sendExamCorrectionNotificationEmail(student.user.standardEmail, student.fullName, exam.name)
            }
        }
    }
}