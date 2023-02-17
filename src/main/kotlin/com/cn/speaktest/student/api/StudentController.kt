package com.cn.speaktest.student.api

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.exam.model.ExamRequest
import com.cn.speaktest.student.model.Student
import com.cn.speaktest.student.payload.response.StudentProfileResponse
import com.cn.speaktest.exam.repository.ExamRequestRepository
import com.cn.speaktest.student.repository.StudentRepository
import com.cn.speaktest.security.api.AuthService
import com.cn.speaktest.security.repository.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/student")
@Validated
class StudentController(
    val authService: AuthService,
    val studentRepository: StudentRepository,
    val userRepository: UserRepository,
    val examRequestRepository: ExamRequestRepository,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun getProfile(@RequestHeader("Authorization") auth: String?): Message {
        return StudentProfileResponse(getStudentByAuthToken(auth)).toOkMessage()
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): Message {
        val student = getStudentByAuthToken(auth)

        if (fullName != null) student.fullName = fullName
        if (biography != null) student.biography = biography

        return StudentProfileResponse(
            studentRepository.save(student)
        ).toOkMessage()
    }

    @PostMapping("/exam-request")
    @PreAuthorize("hasRole('STUDENT')")
    fun examRequest(
        @RequestHeader("Authorization") auth: String?
    ): Message {
        val student = getStudentByAuthToken(auth)

        if (examRequestRepository.existsByStudent(student))
            throw MethodNotAllowedException("You have an unhandled exam yet.")

        examRequestRepository.save(ExamRequest(Date(System.currentTimeMillis()), student))

        return Message(null, "Your exam request submitted successfully")
    }

    private fun getStudentByAuthToken(auth: String?): Student {
        val user = userRepository.findById(
            authService.getUserIdFromAuthorizationHeader(auth)
        ).orElseThrow { NotFoundException("User Not found") }
        return studentRepository.findByUser(user).orElseThrow { NotFoundException("Student Not found") }
    }
}