package com.cn.langujet.domain.student.service

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.user.services.AuthService
import com.cn.langujet.domain.student.model.Student
import com.cn.langujet.domain.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val authService: AuthService,
    private val studentRepository: StudentRepository,
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

    fun getStudentByAuthToken(auth: String?): Student {
        return studentRepository.findByUser_Id(
            authService.getUserByAuthToken(auth).id!!
        ).orElseThrow { NotFoundException("Student Not found") }
    }

    fun getStudentByUserId(userId: String): Student {
        return studentRepository.findByUser_Id(authService.getUserById(userId).id!!).orElseThrow {
            NotFoundException("Student not found")
        }
    }

    fun doesStudentOwnAuthToken(token: String, studentId: String): Boolean {
        return getStudentByAuthToken(token).id == studentId
    }
}