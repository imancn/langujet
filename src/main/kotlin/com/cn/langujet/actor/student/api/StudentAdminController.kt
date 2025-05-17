package com.cn.langujet.actor.student.api

import com.cn.langujet.application.arch.controller.HistoricalEntityViewController
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/students")
@Validated
class StudentAdminController(
    override var service: StudentService
) : HistoricalEntityViewController<StudentService, StudentEntity>()