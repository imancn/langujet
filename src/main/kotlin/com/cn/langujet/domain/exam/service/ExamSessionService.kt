package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamSessionEnrollResponse
import com.cn.langujet.actor.exam.payload.ExamSessionResponse
import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.application.advice.InvalidTokenException
import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.ExamSession
import com.cn.langujet.domain.exam.model.ExamSessionState
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.repository.ExamSessionRepository
import com.cn.langujet.domain.professor.ProfessorService
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExamSessionService(
    private val examSessionRepository: ExamSessionRepository,
    private val examService: ExamService,
    private val sectionService: SectionService,
    private val studentService: StudentService,
    private val professorService: ProfessorService,
) {
    fun enrollExamSession(auth: String, examType: ExamType, sectionType: SectionType?): ExamSessionEnrollResponse {
        val studentId = studentService.getStudentByAuthToken(auth).id!!

        val existsByStudentIdAndStateContaining = examSessionRepository.existsByStudentIdAndStateContaining(
            studentId,
            listOf(
                ExamSessionState.ENROLLED,
                ExamSessionState.STARTED,
            )
        )
        if (existsByStudentIdAndStateContaining) {
            throw MethodNotAllowedException("Finish current enrolled exam")
        }

        /**
         * Todo: Should check user has not another enrolled exam!
         * Todo: Payment should be implemented here later!
         */

        val examId = examService.getRandomExamIdByType(examType)
        val sections = sectionService.getSectionsByExamId(examId)
        val specifiedSection = sections.find { it.sectionType == sectionType }

        val sectionOrders = if (sectionType == null) {
            sections.map { it.order }.sorted()
        } else {
            listOf(
                specifiedSection?.order
                    ?: throw NotFoundException("There is no section with $sectionType in Exam with id: $examId")
            )
        }

        val examSession = examSessionRepository.save(
            ExamSession(
                studentId, null, // this is immediately null, and it means exam professor is AI
                examId, specifiedSection?.id, sectionOrders, Date(System.currentTimeMillis())
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
        val exam = examService.getExamById(examSession.examId)
        return ExamSessionResponse(examSession, exam)
    }

    fun getAllStudentExamSessionResponses(authToken: String): List<ExamSessionResponse> {
        val studentId = studentService.getStudentByAuthToken(authToken).id ?: ""
        return examSessionRepository.findAllByStudentId(studentId).map {
            ExamSessionResponse(it, null)
        }
    }

    fun getAllStudentExamSessionResponsesByState(
        authToken: String,
        state: ExamSessionState
    ): List<ExamSessionResponse> {
        val studentId = studentService.getStudentByAuthToken(authToken).id ?: ""
        return examSessionRepository.findAllByStudentIdAndState(studentId, state).map {
            ExamSessionResponse(it, null)
        }
    }

    fun getProfessorExamSession(
        authToken: String,
        examSessionId: String,
    ): ExamSession {
        val examSession = getExamSessionById(examSessionId)
        if (!professorService.doesProfessorOwnAuthToken(
                authToken, examSession.professorId ?: ""
            )
        ) throw InvalidTokenException("Exam Session with id: $examSessionId is not belong to your token")
        return examSession
    }

    fun getAllProfessorExamSessions(authToken: String): List<ExamSession> {
        val professorId = professorService.getProfessorByAuthToken(authToken).id ?: ""
        return examSessionRepository.findAllByProfessorId(professorId)
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
                val examDuration = (examService.getExamById(examSession.examId).examDuration ?: 0L) * 1000
                it.startDate = now
                it.endDate = Date(now.time + examDuration + loadingDelay)
                it.state = ExamSessionState.STARTED
            })
        }
        return SectionDTO(section.also { it.id = null })
    }

    fun finishExamSession(
        authToken: String, examSessionId: String
    ): ExamSessionResponse {
        return ExamSessionResponse(
            getStudentExamSession(authToken, examSessionId).also { examSession ->
                if (examSession.state == ExamSessionState.ENROLLED)
                    throw MethodNotAllowedException("The exam session has not been started")
                else if (examSession.state.order >= ExamSessionState.FINISHED.order)
                    throw MethodNotAllowedException("The exam session has been finished")
                else {
                    examSessionRepository.save(examSession.also {
                        it.state = ExamSessionState.FINISHED
                    })
                }
            },
            null
        )
    }

    fun checkExamSessionAndSectionAvailability(examSession: ExamSession, sectionOrder: Int) {
        if (!examSession.sectionOrders.contains(sectionOrder))
            throw MethodNotAllowedException("You don't have permission to this section")

        if (examSession.state.order >= ExamSessionState.FINISHED.order)
            throw MethodNotAllowedException("The exam session has been finished")

        if ((examSession.endDate?.time ?: (Long.MAX_VALUE)) < System.currentTimeMillis()) {
            examSessionRepository.save(examSession.also {
                it.state = ExamSessionState.FINISHED
            })
            throw MethodNotAllowedException("The exam session time is over")
        }
    }

    fun getExamSessionById(id: String?): ExamSession {
        return examSessionRepository.findById(id!!).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
}