package com.cn.speaktest.exam.service

import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.exam.api.request.ExamRequest
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.student.service.StudentService
import org.springframework.stereotype.Service

@Service
class ExamRequestService(
    private val examRequestRepository: ExamRequestRepository,
    private val studentService: StudentService,
) {
    fun createExamRequest(studentId: String?, examId: String?): ExamRequest {
        val student = studentService.getStudentByStudentId(studentId!!)

        if (examRequestRepository.existsByStudentAndExamId(student, examId!!))
            throw MethodNotAllowedException("You have an unhandled exam yet.")

        return examRequestRepository.save(ExamRequest(examId, student))
    }

    fun getExamRequestById(id: String?): ExamRequest {
        return examRequestRepository.findById(id!!).orElseThrow {
            throw NotFoundException("ExamRequest with id: $id not found")
        }
    }

    fun getExamRequestsByExamId(examId: String): List<ExamRequest> {
        return examRequestRepository.findByExamId(examId)
    }

    fun getExamRequestsByStudentId(studentId: String): List<ExamRequest> {
        return examRequestRepository.findByStudentId(studentId)
    }

    fun deleteExamRequest(examRequest: ExamRequest) {
        examRequestRepository.delete(examRequest)
    }
}
