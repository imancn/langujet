package com.cn.langujet.actor.student.api

import com.cn.langujet.actor.student.payload.response.StudentProfileResponse
import com.cn.langujet.application.advice.Message
import com.cn.langujet.application.advice.toOkMessage
import com.cn.langujet.domain.student.service.StudentService
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
    fun getProfile(@RequestHeader("Authorization") auth: String?): Message {
        return StudentProfileResponse(studentService.getStudentByAuthToken(auth)).toOkMessage()
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): Message {
        return studentService.editProfile(auth, fullName, biography).toOkMessage()
    }

    @PostMapping("/exam-request")
    @PreAuthorize("hasRole('STUDENT')")
    fun examRequest(
        @RequestParam examId: String,
        @RequestHeader("Authorization") auth: String?
    ): Message {
        studentService.createExamRequest(auth, examId)
        return Message(null, "Your exam request submitted successfully")
    }
}