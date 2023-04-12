package com.cn.speaktest.admin

import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.exam.service.ExamRequestService
import com.cn.speaktest.exam.service.ExamService
import com.cn.speaktest.exam.service.ExamSessionServiceInterface
import com.cn.speaktest.professor.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val examRequestRepository: ExamRequestRepository,
    private val examRequestService: ExamRequestService,
    private val professorRepository: ProfessorRepository,
    private val examSessionService: ExamSessionServiceInterface,
    private val examService: ExamService,
) {

    fun confirmExam(examRequestId: String?, professorId: String?): String? {
        val examRequest = examRequestRepository.findById(examRequestId!!).orElseThrow {
            NotFoundException("ExamRequest not found")
        }
        val professor = professorRepository.findById(professorId!!).orElseThrow {
            NotFoundException("Professor not found")
        }

        return examService.submitExam(examRequest, professor).id
    }

    fun getProfessors() = professorRepository.findAll().sortedBy {
        it.fullName
    }

    fun getExamRequests() = examRequestRepository.findAll().sortedByDescending {
        it.date
    }
}