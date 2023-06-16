package com.cn.langujet.actor.student.api

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.domain.student.service.StudentService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student")
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

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): ResponseEntity<StudentProfileResponse> =
        toOkResponseEntity(studentService.editProfile(auth, fullName, biography))

    @PostMapping("/exam-request")
    @PreAuthorize("hasRole('STUDENT')")
    fun examRequest(
        @RequestParam examId: String,
        @RequestHeader("Authorization") auth: String?
    ): ResponseEntity<String> =
        toOkResponseEntity("Your exam request submitted successfully")
}