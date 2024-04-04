package com.cn.langujet.actor.student.api

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/student")
@Validated
class StudentController(
    val studentService: StudentService,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun getProfile(): ResponseEntity<StudentProfileResponse> {
        return toOkResponseEntity(StudentProfileResponse(studentService.getStudentByUserId(Auth.userId())))
    }
    @PostMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): ResponseEntity<StudentProfileResponse> {
        return toOkResponseEntity(studentService.editProfile(fullName, biography))
    }
}