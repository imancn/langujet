package com.cn.langujet.domain.exam.service

import com.cn.langujet.actor.exam.payload.dto.SectionDto
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.repository.SectionRepository
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository
) {

    fun getSectionById(id: String): SectionDto {
        return SectionDto(
            sectionRepository.findById(id).orElseThrow {
                NotFoundException("Section with id $id not found")
            }
        )
    }

    fun createSection(section: SectionDto): Section {
        return sectionRepository.save(Section(section))
    }

    fun updateSection(id: String, section: SectionDto): SectionDto? {
        val existingSection = Section(getSectionById(id))
        section.name?.let { existingSection.name = it }
        section.order?.let { existingSection.order = it }
        return SectionDto(sectionRepository.save(existingSection))
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