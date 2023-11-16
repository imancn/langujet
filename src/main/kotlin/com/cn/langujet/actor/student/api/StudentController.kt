package com.cn.langujet.actor.student.api

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
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
    fun getProfile(
        @RequestHeader("Authorization") auth: String?
    ): ResponseEntity<StudentProfileResponse> =
        toOkResponseEntity(StudentProfileResponse(studentService.getStudentByAuthToken(auth)))

    @PostMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): ResponseEntity<StudentProfileResponse> =
        toOkResponseEntity(studentService.editProfile(auth, fullName, biography))
}