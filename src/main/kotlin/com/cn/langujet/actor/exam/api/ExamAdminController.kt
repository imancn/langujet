package com.cn.langujet.actor.exam.api

import com.cn.langujet.application.arch.controller.HistoricalEntityCrudController
import com.cn.langujet.application.arch.controller.payload.response.MessageResponse
import com.cn.langujet.domain.exam.model.ExamEntity
import com.cn.langujet.domain.exam.service.ExamService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/exams")
@Validated
class ExamAdminController(
    override var service: ExamService
) : HistoricalEntityCrudController<ExamService, ExamEntity>() {
    
    @PostMapping("activate/{id}")
    fun activate(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(
            bundle.getMessageResponse(
                if (service.activate(id)) {
                    "successful"
                } else {
                    "failed"
                }
            )
        )
    }
    
    @PostMapping("deactivate/{id}")
    fun deactivate(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(
            bundle.getMessageResponse(
                if (service.deactivate(id)) {
                    "successful"
                } else {
                    "failed"
                }
            )
        )
    }
}