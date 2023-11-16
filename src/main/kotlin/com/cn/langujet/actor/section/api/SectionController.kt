package com.cn.langujet.actor.section.api

import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.actor.util.toCreatedResponseEntity
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.exam.model.Section
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("api/v1/admin/section")
class SectionController(private val sectionService: SectionService) {

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionById(
        @PathVariable id: String
    ): ResponseEntity<Section> = toOkResponseEntity(sectionService.getSectionById(id))

    @GetMapping("by-exam-id/{examId}")
//    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionByExamId(
        @PathVariable examId: String
    ): ResponseEntity<List<Section>> =
        toOkResponseEntity(sectionService.getSectionByExamId(examId))

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    fun createSection(
        @RequestBody section: SectionDTO
    ): ResponseEntity<Section> = sectionService.createSection(section.toSection()).let {
        toCreatedResponseEntity(it, URI("/sections/${it.id}"))
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    fun updateSection(
        @PathVariable id: String,
        @RequestBody section: SectionDTO
    ): ResponseEntity<Section> =
        toOkResponseEntity(sectionService.updateSection(id, section.toSection()))

    @DeleteMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
    fun deleteSection(
        @PathVariable id: String
    ): ResponseEntity<String> =
        if (sectionService.deleteSection(id)) toOkResponseEntity("Section deleted")
        else throw NotFoundException("Section with id: $id not found")
}