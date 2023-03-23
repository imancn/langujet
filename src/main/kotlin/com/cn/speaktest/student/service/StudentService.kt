package com.cn.speaktest.student.service

import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.exam.model.ExamRequest
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.security.api.AuthService
import com.cn.speaktest.student.model.Student
import com.cn.speaktest.student.payload.response.StudentProfileResponse
import com.cn.speaktest.student.repository.StudentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    val authService: AuthService,
    val studentRepository: StudentRepository,
    val examRequestRepository: ExamRequestRepository,
) {

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

    fun examRequest(auth: String?) {
        val student = this.getStudentByAuthToken(auth)

        if (examRequestRepository.existsByStudent(student))
            throw MethodNotAllowedException("You have an unhandled exam yet.")

        examRequestRepository.save(ExamRequest(Date(System.currentTimeMillis()), student))
    }

    fun getStudentByAuthToken(auth: String?): Student {
        return studentRepository.findByUser_Id(
            authService.getUserByAuthToken(auth).id!!
        ).orElseThrow { NotFoundException("Student Not found") }
    }
}