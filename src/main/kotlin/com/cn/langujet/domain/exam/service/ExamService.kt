package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.repository.ExamRepository
import org.springframework.stereotype.Service

@Service
class ExamService(
    private val examValidatorService: ExamValidatorService,
) : HistoricalEntityService<ExamRepository, ExamEntity>() {
    fun activate(id: Long): Boolean {
        val exam = getById(id)
        examValidatorService.validate(exam)
        return true
    }
    
    fun deactivate(id: Long): Boolean {
        val exam = getById(id)
        examValidatorService.validate(exam)
        return true
    }
}