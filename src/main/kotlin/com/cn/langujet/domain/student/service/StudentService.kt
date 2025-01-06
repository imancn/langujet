package com.cn.langujet.domain.student.service

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {
    fun editProfile(fullName: String?, biography: String?): StudentProfileResponse {
        val student = getStudentByUserId(Auth.userId())
        
        if (!fullName.isNullOrBlank()) student.fullName = fullName
        if (!biography.isNullOrBlank()) student.biography = biography
        
        return StudentProfileResponse(
            studentRepository.save(student)
        )
    }
    
    fun getStudentByUserId(userId: String): StudentEntity {
        return studentRepository.findByUser_Id(userId).orElseThrow {
            UnprocessableException("Student not found")
        }
    }
}