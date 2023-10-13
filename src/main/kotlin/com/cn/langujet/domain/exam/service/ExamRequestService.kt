package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.ExamRequest
import com.cn.langujet.domain.exam.model.ExamType
import com.cn.langujet.domain.exam.model.SectionType
import com.cn.langujet.domain.exam.repository.ExamRequestRepository
import com.cn.langujet.domain.security.services.AuthService
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.stereotype.Service

@Service
class ExamRequestService(
    private val authService: AuthService,
    private val examRequestRepository: ExamRequestRepository,
    private val studentService: StudentService,
) {
    fun createExamRequest(auth: String, examType: ExamType, sectionType: SectionType?): ExamRequest {
        val student = studentService.getStudentByUserId(
            authService.getUserIdFromAuthorizationHeader(auth)
        )
        if (examRequestRepository.existsByStudentIdAndExamTypeAndSectionType(
                student.id ?: "",
                examType,
                sectionType
            )
        ) throw MethodNotAllowedException("You have an unhandled exam yet.")
        return examRequestRepository.save(ExamRequest(examType, sectionType, student.id ?: ""))
    }

    fun getExamRequestById(id: String?): ExamRequest {
        return examRequestRepository.findById(id!!).orElseThrow {
            throw NotFoundException("ExamRequest with id: $id not found")
        }
    }

    fun getExamRequests(auth: String): List<ExamRequest> {
        val student = studentService.getStudentByUserId(
            authService.getUserIdFromAuthorizationHeader(auth)
        )
        return examRequestRepository.findByStudentId(student.id ?: "")
    }

    fun deleteExamRequest(examRequest: ExamRequest) {
        examRequestRepository.delete(examRequest)
    }
}
