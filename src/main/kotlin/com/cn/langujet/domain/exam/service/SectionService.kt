package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.InvalidInputException
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.SectionEntity
import com.cn.langujet.domain.exam.repository.SectionCustomRepository
import com.cn.langujet.domain.exam.repository.SectionRepository
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
    private val sectionCustomRepository: SectionCustomRepository
) {
    fun getSectionById(id: String): SectionEntity {
        return sectionRepository.findById(id).orElseThrow {
            NotFoundException("Section with id $id not found")
        }
    }

    fun getSectionsByExamId(examId: String): List<SectionEntity> {
        return sectionRepository.findAllByExamId(examId)
    }

    fun getSectionByExamIdAndOrder(examId: String, order: Int): SectionEntity {
        return sectionRepository.findByExamIdAndOrder(examId, order).orElseThrow {
            throw NotFoundException("Section not found")
        }
    }

    fun createSection(section: SectionEntity): SectionEntity {
        if (sectionRepository.existsByExamIdAndOrder(section.examId, section.order)) {
            throw UnprocessableException("Section with order ${section.order} already exists.")
        }
        return sectionRepository.save(
            section.also {
                it.id = null
            }
        )
    }

    fun updateSection(section: SectionEntity): SectionEntity {
        if (section.id.isNullOrBlank()) throw InvalidInputException("Section Id is empty")
        val existingSection = getSectionById(section.id ?: "")
        section.examId.let { existingSection.examId = it }
        section.header.let { existingSection.header = it }
        section.order.let { existingSection.order = it }
        section.sectionType.let { existingSection.sectionType = it }
        section.parts.let { existingSection.parts = it }
        return sectionRepository.save(existingSection)
    }

    fun deleteSection(id: String): Boolean {
        val section = sectionRepository.findById(id).orElse(null)
        return if (section != null) {
            sectionRepository.delete(section)
            true
        } else {
            false
        }
    }
    
    fun getSectionsMetaData(examId: String): List<SectionMetaDTO> {
        return sectionCustomRepository.findSectionsMetaDataByExamId(examId)
    }
}