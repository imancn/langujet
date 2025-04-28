package com.cn.langujet.actor.service

import com.cn.langujet.actor.service.payload.GetAvailableExamServicesRequest
import com.cn.langujet.actor.service.payload.GetAvailableExamServicesResponse
import com.cn.langujet.application.arch.controller.payload.response.PageResponse
import com.cn.langujet.domain.service.service.ServiceService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/student/services")
@Validated
class ServiceStudentController(
    private val serviceService: ServiceService
) {
    @PostMapping("/exams/search")
    fun getAvailableExamServices(
        @RequestBody request: GetAvailableExamServicesRequest
    ): PageResponse<GetAvailableExamServicesResponse> {
        return serviceService.getAvailableExamServices(request)
    }
}