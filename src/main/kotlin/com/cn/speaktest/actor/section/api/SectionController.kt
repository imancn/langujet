package com.cn.speaktest.actor.section.api

import com.cn.speaktest.actor.exam.payload.dto.SectionDto
import com.cn.speaktest.domain.exam.model.Section
import com.cn.speaktest.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/sections")
class SectionController(private val sectionService: SectionService) {

    @GetMapping("/{id}")
    fun getSectionById(@PathVariable id: String): ResponseEntity<SectionDto> {
        return ResponseEntity.ok(sectionService.getSectionById(id))
    }

    @PostMapping
    fun createSection(@RequestBody section: SectionDto): ResponseEntity<Section> {
        val savedSection = sectionService.createSection(section)
        return ResponseEntity.created(URI("/sections/${savedSection.id}")).body(savedSection)
    }

    @PutMapping("/{id}")
    fun updateSection(@PathVariable id: String, @RequestBody section: SectionDto): ResponseEntity<SectionDto> {
        val updatedSection = sectionService.updateSection(id, section)
        return if (updatedSection != null) {
            ResponseEntity.ok(updatedSection)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSection(@PathVariable id: String): ResponseEntity<Void> {
        val deleted = sectionService.deleteSection(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}