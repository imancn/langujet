package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.SectionComposite
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.repository.SectionCustomRepository
import com.cn.langujet.domain.exam.repository.SectionRepository
import com.cn.langujet.domain.exam.repository.dto.SectionMetaDTO
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class SectionService(
    override var repository: SectionRepository,
    private val sectionCustomRepository: SectionCustomRepository,
    private val questionService: QuestionService,
    private val partService: PartService
) : HistoricalEntityService<SectionRepository, SectionEntity>() {
    fun getSectionCompositeById(id: Long): SectionComposite {
        val section = repository.findById(id).orElseThrow {
            UnprocessableException("Section with id $id not found")
        }
        val criteria = Criteria.where("sectionId").`is`(section.id)
        return SectionComposite(
            section = section,
            questions = questionService.find(criteria),
            parts = partService.find(criteria)
        )
    }
    
    fun getSectionsByExamId(examId: Long): List<SectionEntity> {
        return repository.findAllByExamId(examId)
    }
    
    fun getSectionByExamIdAndOrder(examId: Long, order: Int): SectionEntity {
        return repository.findByExamIdAndOrder(examId, order).orElseThrow {
            throw UnprocessableException("Section not found")
        }
    }

    fun createSection(section: SectionEntity): SectionEntity {
        if (repository.existsByExamIdAndOrder(section.examId, section.order)) {
            throw UnprocessableException("Section with order ${section.order} already exists.")
        }
        return save(
            section.also {
                it.id = null
            }
        )
    }

    fun updateSection(section: SectionEntity): SectionEntity {
        if (section.id == null) throw InvalidInputException("Section Id is empty")
        val existingSection = repository.findById(section.id ?: Entity.UNKNOWN_ID)
            .getOrElse { throw NoSuchElementException("Section with id ${section.id} not found") }
        section.examId.let { existingSection.examId = it }
        section.header.let { existingSection.header = it }
        section.order.let { existingSection.order = it }
        section.sectionType.let { existingSection.sectionType = it }
        return save(existingSection)
    }
    
    fun deleteSection(id: Long): Boolean {
        val section = repository.findById(id).orElse(null)
        return if (section != null) {
            repository.delete(section)
            true
        } else {
            false
        }
    }
    
    fun getSectionsMetaData(examId: Long): List<SectionMetaDTO> {
        return sectionCustomRepository.findSectionsMetaDataByExamId(examId)
    }
}