package com.cn.langujet.actor.section.api

import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/admin/section")
class SectionController(private val sectionService: SectionService) {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionById(
        @PathVariable id: String
    ): SectionDTO = SectionDTO(sectionService.getSectionById(id))

    @GetMapping("by-exam-id/{examId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionByExamId(
        @PathVariable examId: String
    ): List<SectionDTO> =
        sectionService.getSectionByExamId(examId).map { SectionDTO(it) }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createSection(
        @RequestBody section: SectionDTO
    ): SectionDTO = SectionDTO(sectionService.createSection(section.toSection()))

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSection(
        @PathVariable id: String,
        @RequestBody section: SectionDTO
    ): SectionDTO = SectionDTO(sectionService.updateSection(id, section.toSection()))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSection(
        @PathVariable id: String
    ): ResponseEntity<String> =
        if (sectionService.deleteSection(id)) toOkResponseEntity("Section deleted")
        else throw NotFoundException("Section with id: $id not found")
}