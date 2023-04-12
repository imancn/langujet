package com.cn.speaktest.student.service

import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.exam.api.request.ExamRequest
import com.cn.speaktest.exam.service.ExamRequestService
import com.cn.speaktest.security.api.AuthService
import com.cn.speaktest.student.model.Student
import com.cn.speaktest.student.payload.response.StudentProfileResponse
import com.cn.speaktest.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val authService: AuthService,
    private val studentRepository: StudentRepository,
) {
    private lateinit var examRequestService: ExamRequestService

    fun editProfile(
        auth: String?, fullName: String?, biography: String?
    ): StudentProfileResponse {
        val student = this.getStudentByAuthToken(auth)

        if (!fullName.isNullOrBlank()) student.fullName = fullName
        if (!biography.isNullOrBlank()) student.biography = biography

        return StudentProfileResponse(
            studentRepository.save(student)
        )
    }

    fun createExamRequest(auth: String?, examId: String): ExamRequest {
        return examRequestService.createExamRequest(examId, getStudentByAuthToken(auth).id)
    }

    fun getStudentByAuthToken(auth: String?): Student {
        return studentRepository.findByUser_Id(
            authService.getUserByAuthToken(auth).id!!
        ).orElseThrow { NotFoundException("Student Not found") }
    }

    fun getStudentByStudentId(studentId: String): Student {
        return studentRepository.findById(studentId).orElseThrow {
            throw NotFoundException("Student not found")
        }
    }

    fun getStudentByUserId(userId: String): Student {
        return studentRepository.findByUser_Id(authService.getUserById(userId).id!!).orElseThrow {
            throw NotFoundException("Student not found")
        }
    }
}