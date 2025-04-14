package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.repository.SectionCustomRepository
import com.cn.langujet.domain.exam.repository.SectionRepository
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
    private val sectionCustomRepository: SectionCustomRepository,
    private val questionService: QuestionService,
    private val partService: PartService
) {
    fun getSectionById(id: String): SectionDTO {
        val section = sectionRepository.findById(id).orElseThrow {
            UnprocessableException("Section with id $id not found")
        }
        val criteria = Criteria.where("sectionId").`is`(section.id)
        return SectionDTO(
            section = section,
            questions = questionService.find(criteria),
            parts = partService.find(criteria)
        )
    }

    fun getSectionsByExamId(examId: String): List<SectionEntity> {
        return sectionRepository.findAllByExamId(examId)
    }

    fun getSectionByExamIdAndOrder(examId: String, order: Int): SectionEntity {
        return sectionRepository.findByExamIdAndOrder(examId, order).orElseThrow {
            throw UnprocessableException("Section not found")
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
        val existingSection = sectionRepository.findById(section.id ?: "")
            .getOrElse { throw NoSuchElementException("Section with id ${section.id} not found") }
        section.examId.let { existingSection.examId = it }
        section.header.let { existingSection.header = it }
        section.order.let { existingSection.order = it }
        section.sectionType.let { existingSection.sectionType = it }
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