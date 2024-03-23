package com.cn.langujet.domain.result.service

import com.cn.langujet.domain.result.model.SectionResult
import com.cn.langujet.domain.result.repository.SectionResultRepository
import org.springframework.stereotype.Service

@Service
class SectionResultService(
    private val sectionResultRepository: SectionResultRepository,
) {
    fun createSectionResult(sectionResult: SectionResult) {
        sectionResultRepository.save(sectionResult)
    }
    
    fun getSectionResultsByResultId(resultId: String): List<SectionResult> {
        return sectionResultRepository.findAllByResultId(resultId)
    }
}