package com.cn.langujet.actor.section.api

import com.cn.langujet.actor.exam.payload.dto.SectionDto
import com.cn.langujet.actor.util.toCreatedResponseEntity
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/sections")
class SectionController(private val sectionService: SectionService) {

    @GetMapping("/{id}")
    fun getSectionById(
        @PathVariable id: String
    ): ResponseEntity<SectionDto> = toOkResponseEntity(sectionService.getSectionById(id))

    @PostMapping
    fun createSection(
        @RequestBody section: SectionDto
    ): ResponseEntity<Section> = sectionService.createSection(section).let {
        toCreatedResponseEntity(it, URI("/sections/${it.id}"))
    }

    @PutMapping("/{id}")
    fun updateSection(
        @PathVariable id: String,
        @RequestBody section: SectionDto
    ): ResponseEntity<SectionDto> =
        toOkResponseEntity(sectionService.updateSection(id, section))

    @DeleteMapping("/{id}")
    fun deleteSection(
        @PathVariable id: String
    ): ResponseEntity<String> =
        if (sectionService.deleteSection(id)) toOkResponseEntity("Section deleted")
        else throw NotFoundException("Section with id: $id not found")
}