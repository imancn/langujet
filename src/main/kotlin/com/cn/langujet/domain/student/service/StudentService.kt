package com.cn.langujet.domain.student.service

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    override var repository: StudentRepository
) : HistoricalEntityService<StudentRepository, StudentEntity>() {
    
    fun editProfile(fullName: String?, biography: String?): StudentProfileResponse {
        val student = getStudentByUserId(Auth.userId())
        
        if (!fullName.isNullOrBlank()) student.fullName = fullName
        if (!biography.isNullOrBlank()) student.biography = biography
        
        return StudentProfileResponse(
            repository.save(student)
        )
    }
    
    fun getStudentByUserId(userId: Long): StudentEntity {
        return repository.findByUser_Id(userId).orElseThrow {
            UnprocessableException("Student not found")
        }
    }
}