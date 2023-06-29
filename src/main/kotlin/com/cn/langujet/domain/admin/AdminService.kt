package com.cn.langujet.domain.admin

import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.repository.ExamRequestRepository
import com.cn.langujet.domain.exam.service.ExamRequestService
import com.cn.langujet.domain.exam.service.ExamSessionServiceInterface
import com.cn.langujet.domain.professor.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val examRequestRepository: ExamRequestRepository,
    private val examRequestService: ExamRequestService,
    private val professorRepository: ProfessorRepository,
    private val examSessionService: ExamSessionServiceInterface,
) {

    fun confirmExamRequest(examRequestId: String?, professorId: String?): String? {
        val examRequest = examRequestService.getExamRequestById(examRequestId)
        val professor = professorRepository.findById(professorId!!).orElseThrow {
            NotFoundException("Professor not found")
        }
        return examSessionService.enrollExamSession(examRequest, professor).id
    }

    fun getProfessors() = professorRepository.findAll().sortedBy {
        it.fullName
    }

    fun getExamRequests() = examRequestRepository.findAll().sortedByDescending {
        it.date
    }
}