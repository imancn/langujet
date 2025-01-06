package com.cn.langujet.actor.section.api

import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.UnprocessableException
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/admin/sections")
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
        sectionService.getSectionsByExamId(examId).map { SectionDTO(it) }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createSection(
        @RequestBody section: SectionDTO
    ): SectionDTO = SectionDTO(sectionService.createSection(section.toSection()))

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSection(
        @RequestBody section: SectionDTO
    ): SectionDTO = SectionDTO(sectionService.updateSection(section.toSection()))

    @PostMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSection(
        @PathVariable id: String
    ): ResponseEntity<String> =
        if (sectionService.deleteSection(id)) toOkResponseEntity("Section deleted")
        else throw UnprocessableException("Section with id: $id not found")
}