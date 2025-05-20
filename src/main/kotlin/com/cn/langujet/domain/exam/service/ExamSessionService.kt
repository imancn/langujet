package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.*
import com.cn.langujet.application.arch.Auth
import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
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
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExamSessionService(
    override var repository: ExamSessionRepository,
    private val examSessionCustomRepository: ExamSessionCustomRepository,
    private val examService: ExamService,
    private val sectionService: SectionService,
    private val correctionService: CorrectionService,
    private val examGeneratorService: ExamGeneratorService,
    private val serviceService: ServiceService,
    private val userService: UserService,
    private val mailSenderService: MailSenderService,
    private val studentService: StudentService,
    private val partService: PartService,
    private val questionService: QuestionService
) : HistoricalEntityService<ExamSessionRepository, ExamSessionEntity>() {
    fun getExamSessionById(id: Long): ExamSessionEntity {
        return repository.findById(id).orElseThrow {
            UnprocessableException("ExamSession with id: $id not found")
        }
    }
    
    fun getStudentExamSession(
        examSessionId: Long,
    ): ExamSessionEntity {
        val examSession = getExamSessionById(examSessionId)
        if (Auth.userId() != examSession.studentUserId) {
            throw InvalidCredentialException("Exam Session with id: $examSessionId is not belong to your token")
        }
        return examSession
    }
    
    fun getStudentExamSessionDetailsResponse(examSessionId: Long): ExamSessionDetailsResponse {
        val examSession = getStudentExamSession(examSessionId)
        val sections = sectionService.getSectionsMetaData(examSession.examId).sortedBy { it.order }
        return ExamSessionDetailsResponse(sections)
    }
    
    fun searchExamSessions(request: ExamSessionSearchStudentRequest): PageResponse<ExamSessionSearchResponse> {
        return examSessionCustomRepository.searchExamSessions(
            request, Auth.userId()
        )
    }
    
    fun searchExamSessions(request: ExamSessionSearchAdminRequest): PageResponse<ExamSessionSearchResponse> {
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
            ), userService.getUserByEmail(request.studentEmail).id ?: Entity.UNKNOWN_ID
        )
    }
    
    
    fun enrollExamSessionByEmail(email: String, examServiceId: Long, examId: Long? = null): ExamSessionEnrollResponse {
        val userId = userService.getUserByEmail(email).id ?: throw UnprocessableException("User not found")
        return enrollExamSession(userId, examServiceId, examId)
    }
    
    fun enrollExamSession(userId: Long, examServiceId: Long, examId: Long? = null): ExamSessionEnrollResponse {
        val service = serviceService.getById(examServiceId) as ServiceEntity.ExamServiceEntity
        val exam = if (examId != null) {
            examService.getById(examId).let {
                if (service.examMode != it.mode || service.examType != it.type) {
                    throw UnprocessableException("Exam and Exam Service are not compatible")
                } else {
                    it
                }
            }
        } else {
            examGeneratorService.getRandomStudentAvailableExam(userId, service)
        }
        val examSession = save(
            ExamSessionEntity(
                userId,
                exam.id ?: Entity.UNKNOWN_ID,
                exam.type,
                exam.mode,
                service.correctorType,
                Date(System.currentTimeMillis())
            )
        )
        return ExamSessionEnrollResponse(examSession)
    }
    
    fun getExamSection(
        examSessionId: Long, sectionOrder: Int
    ): SectionComposite {
        val examSession = repository.findByStudentUserIdAndState(
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
            save(examSession.also {
                it.startDate = Date(System.currentTimeMillis())
                it.state = ExamSessionState.STARTED
            })
        }
        val criteria = Criteria.where("sectionId").`is`(section.id)
        val parts = partService.find(criteria)
        val questions = questionService.find(criteria)
        return SectionComposite(
            section = section, parts = parts, questions = questions
        ).also { it.id = null; it.examId = null }
    }
    
    fun finishExamSession(examSessionId: Long): ExamSessionFinishResponse {
        var examSession = getStudentExamSession(examSessionId)
        if (examSession.state == ExamSessionState.ENROLLED) {
            throw UnprocessableException("The exam session has not been started")
        }
        if (examSession.state.order >= ExamSessionState.FINISHED.order) {
            return ExamSessionFinishResponse(examSession.state)
        }
        examSession = save(examSession.also {
            it.state = ExamSessionState.FINISHED
            it.endDate = Date(System.currentTimeMillis())
        })
        correctionService.initiateExamSessionCorrection(examSession)
        examSession = getExamSessionById(examSession.id ?: Entity.UNKNOWN_ID)
        return ExamSessionFinishResponse(examSession.state)
    }
    
    fun finalizeCorrection(examSessionId: Long) {
        val examSession = getExamSessionById(examSessionId)
        if (examSession.state == ExamSessionState.FINISHED) {
            examSession.state = ExamSessionState.CORRECTED
            examSession.correctionDate = Date(System.currentTimeMillis())
            val exam = examService.getById(examSession.examId)
            save(examSession).run {
                val student = studentService.getStudentByUserId(examSession.studentUserId)
                mailSenderService.sendExamCorrectionNotificationEmail(
                    student.user.username, student.fullName, exam.name
                )
            }
        }
    }
}