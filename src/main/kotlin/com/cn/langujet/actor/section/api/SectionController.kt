package com.cn.langujet.actor.section.api

import com.cn.langujet.actor.exam.payload.SectionDTO
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.domain.exam.model.section.SectionEntity
import com.cn.langujet.domain.exam.service.PartService
import com.cn.langujet.domain.exam.service.QuestionService
import com.cn.langujet.domain.exam.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/admin/sections")
class SectionController(
    private val sectionService: SectionService,
    private val questionService: QuestionService,
    private val partService: PartService
) {
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionById(
        @PathVariable id: Long
    ): SectionDTO = sectionService.getSectionById(id)

    @GetMapping("by-exam-id/{examId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSectionByExamId(
        @PathVariable examId: Long
    ): List<SectionEntity> = sectionService.getSectionsByExamId(examId)

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createSection(
        @RequestBody dto: SectionDTO
    ): SectionDTO {
        val section = sectionService.createSection(dto.toSection())
        val sectionId = section.id ?: throw UnprocessableException("There is no section")
        dto.parts.forEach { partDto ->
            val part = partService.create(partDto.toPart(section.examId, sectionId))
            val partId = part.id ?: throw UnprocessableException("There is no section")
            questionService.createMany(
                partDto.getQuestions().map { questionDto -> questionDto.toQuestion(section.examId, sectionId, partId) }
            )
        }
        return getSectionById(section.id ?: throw UnprocessableException("Failed to create section."))
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSection(
        @PathVariable id: Long
    ): ResponseEntity<String> =
        if (sectionService.deleteSection(id)) toOkResponseEntity("Section deleted")
        else throw UnprocessableException("Section with id: $id not found")
}