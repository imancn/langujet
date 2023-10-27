package com.cn.langujet.domain.exam.service

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
    fun enrollExamSession(auth: String, examType: ExamType, sectionType: SectionType?): ExamSession {
        val studentId = studentService.getStudentByAuthToken(auth).id!!
        /**
         * Todo: Should check user has not another enrolled exam!
         * Todo: Payment should be implemented here later!
         */
        val examId = examService.getRandomExamIdByType(examType)
        val sectionId = if (sectionType != null) {
            sectionService.getSectionsByExamId(examId).find {
                it.sectionType == sectionType
            }?.id ?: throw NotFoundException("There is no section with $sectionType in Exam with id: $examId")
        } else null
        return examSessionRepository.save(
            ExamSession(
                studentId, null, // this is immediately null, and it means exam professor is AI
                examId, sectionId, Date(System.currentTimeMillis())
            )
        )
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

    fun getStudentExamSessions(authToken: String): List<ExamSession> {
        val studentId = studentService.getStudentByAuthToken(authToken).id ?: ""
        return examSessionRepository.findAllByStudentId(studentId)
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

    fun getProfessorExamSessions(authToken: String): List<ExamSession> {
        val professorId = professorService.getProfessorByAuthToken(authToken).id ?: ""
        return examSessionRepository.findAllByProfessorId(professorId)
    }

    fun startExamSession(
        authToken: String, examSessionId: String
    ): SectionDTO {
        val examSession = getStudentExamSession(authToken, examSessionId)

        if (examSession.state.order >= ExamSessionState.STARTED.order) throw MethodNotAllowedException("Exam Session has been started once")
        else {
            examSessionRepository.save(examSession.also {
                val now = Date(System.currentTimeMillis())
                val examDuration = examService.getExamById(examSession.examId).examDuration
                it.startDate = now
                it.endDate = Date(now.time + examDuration)
                it.state = ExamSessionState.STARTED
            })
            return SectionDTO(sectionService.getSectionByExamIdAndOrder(examSession.examId, 0))
        }
    }

    fun nextExamSection(
        authToken: String, examSessionId: String, currentSectionOrder: Int
    ): SectionDTO {
        val examSession = getExamSessionIfIsInProgress(authToken, examSessionId)
        val section = try {
            sectionService.getSectionByExamIdAndOrder(examSession.examId, currentSectionOrder + 1)
        } catch (ex: NotFoundException) {
            examSessionRepository.save(examSession.also { it.state = ExamSessionState.FINISHED })
            throw MethodNotAllowedException("There is no section left")
        }
        return SectionDTO(section)
    }

    fun finishExamSession(
        authToken: String, examSessionId: String
    ): ExamSession {
        return getExamSessionIfIsInProgress(authToken, examSessionId).also { examSession ->
            if (examSession.state != ExamSessionState.FINISHED) examSessionRepository.save(examSession.also {
                it.state = ExamSessionState.FINISHED
            })
        }
    }

    fun isExamAvailable(examSessionId: String): Boolean {
        return getExamSessionById(examSessionId).state == ExamSessionState.STARTED
    }

    private fun getExamSessionIfIsInProgress(authToken: String, examSessionId: String): ExamSession {
        val examSession = getStudentExamSession(authToken, examSessionId)
        if (examSession.state == ExamSessionState.ENROLLED) throw MethodNotAllowedException("The exam session has not been started")
        else if (examSession.state.order >= ExamSessionState.FINISHED.order) throw MethodNotAllowedException("The exam session has been finished")
        else if (examSession.endDate?.time!! < System.currentTimeMillis()) {
            examSessionRepository.save(examSession.also {
                it.state = ExamSessionState.FINISHED
            })
            throw MethodNotAllowedException("The exam session time is over")
        }
        return examSession
    }

    fun getExamSessionById(id: String?): ExamSession {
        return examSessionRepository.findById(id!!).orElseThrow {
            NotFoundException("ExamSession with id: $id not found")
        }
    }
}