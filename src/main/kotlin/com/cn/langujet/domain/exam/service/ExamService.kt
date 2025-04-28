package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.ExamEntity
import org.springframework.stereotype.Service

@Service
class ExamService(
    private val examValidatorService: ExamValidatorService,
) : HistoricalEntityService<ExamEntity>() {
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