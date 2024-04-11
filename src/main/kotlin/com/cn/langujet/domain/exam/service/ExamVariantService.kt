package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.ExamVariantCreateRequest
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.ExamVariantEntity
import com.cn.langujet.domain.exam.repository.ExamVariantRepository
import org.springframework.stereotype.Service

@Service
class ExamVariantService(
    private val examVariantRepository: ExamVariantRepository
) {
    fun createExamVariant(examVariantCreateRequest: ExamVariantCreateRequest): ExamVariantEntity {
        return examVariantRepository.save(
            examVariantCreateRequest.toEntity()
        )
    }

    fun getAllExamVariantList(): List<ExamVariantEntity> {
        return examVariantRepository.findAll()
    }

    fun getExamVariantById(id: String): ExamVariantEntity {
        return examVariantRepository.findById(id).orElseThrow {
            throw NotFoundException("Exam Type does not exist")
        }
    }
    
    fun getExamVariantByIds(ids: List<String>): List<ExamVariantEntity> {
        return examVariantRepository.findAllByIdIn(ids)
    }
}
