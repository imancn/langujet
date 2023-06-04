package com.cn.speaktest.domain.admin

import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.domain.exam.repository.ExamRequestRepository
import com.cn.speaktest.domain.exam.service.ExamRequestService
import com.cn.speaktest.domain.exam.service.ExamService
import com.cn.speaktest.domain.exam.service.ExamSessionServiceInterface
import com.cn.speaktest.domain.professor.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val examRequestRepository: ExamRequestRepository,
    private val examRequestService: ExamRequestService,
    private val professorRepository: ProfessorRepository,
    private val examSessionService: ExamSessionServiceInterface,
    private val examService: ExamService,
) {

    fun confirmExamRequest(examRequestId: String?, professorId: String?): String? {
        val examRequest = examRequestService.getExamRequestById(examRequestId)
        val exam = examService.getExamById(examRequest.examInfo.id)
        val professor = professorRepository.findById(professorId!!).orElseThrow {
            NotFoundException("Professor not found")
        }
        return examSessionService.enrollExamSession(examRequest, professor, exam).id
    }

    fun getProfessors() = professorRepository.findAll().sortedBy {
        it.fullName
    }

    fun getExamRequests() = examRequestRepository.findAll().sortedByDescending {
        it.date
    }
}