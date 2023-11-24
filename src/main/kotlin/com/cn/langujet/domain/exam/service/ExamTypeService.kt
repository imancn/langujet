package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamTypeRequest
import com.cn.langujet.actor.exam.payload.ExamTypeResponse
import com.cn.langujet.actor.exam.payload.ExamTypeResponse.Companion.toExamTypeResponse
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.ExamTypeEntity
import com.cn.langujet.domain.exam.repository.ExamTypeRepository
import org.springframework.stereotype.Service

@Service
class ExamTypeService(
    private val examTypeRepository: ExamTypeRepository
) {
    fun createExamType(examTypeRequest: ExamTypeRequest): ExamTypeEntity {
        return examTypeRepository.save(
            examTypeRequest.toEntity().also { it.id = null }
        )
    }

    fun updateExamType(id: String, examTypeRequest: ExamTypeRequest): ExamTypeEntity {
        return examTypeRepository.save(
            examTypeRequest.toEntity().also { it.id = id }
        )
    }

    fun getAllExamTypeList(): List<ExamTypeEntity> {
        return examTypeRepository.findAll()
    }

    fun getExamTypeById(id: String): ExamTypeEntity {
        return examTypeRepository.findById(id).orElseThrow {
            throw NotFoundException("Exam Type does not exist")
        }
    }

    fun getAllAvailableExamTypeList(): List<ExamTypeResponse> {
        return examTypeRepository.findAllByActiveOrderByOrder(true).map {
            it.toExamTypeResponse()
        }
    }
}
