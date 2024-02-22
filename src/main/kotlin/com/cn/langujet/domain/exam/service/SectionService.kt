package com.cn.langujet.domain.exam.service

import com.cn.langujet.application.advice.MethodNotAllowedException
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.repository.SectionRepository
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository
) {
    fun getSectionByExamId(examId: String): List<Section> {
        return sectionRepository.findAllByExamId(examId)
    }

    fun getSectionById(id: String): Section {
        return sectionRepository.findById(id).orElseThrow {
            NotFoundException("Section with id $id not found")
        }
    }

    fun getSectionsByExamId(examId: String): List<Section> {
        return sectionRepository.findAllByExamId(examId)
    }

    fun getSectionByExamIdAndOrder(examId: String, order: Int): Section {
        return sectionRepository.findByExamIdAndOrder(examId, order).orElseThrow {
            throw NotFoundException("Section not found")
        }
    }

    fun createSection(section: Section): Section {
        if (sectionRepository.existsByExamIdAndOrder(section.examId, section.order)) {
            throw MethodNotAllowedException("Section with order ${section.order} already exists.")
        }
        return sectionRepository.save(
            section.also {
                it.id = null
            }
        )
    }

    fun updateSection(id: String, section: Section): Section {
        val existingSection = getSectionById(id)
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
}