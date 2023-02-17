package com.cn.speaktest.controller

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.model.ExamRequest
import com.cn.speaktest.model.Student
import com.cn.speaktest.payload.response.user.StudentProfileResponse
import com.cn.speaktest.repository.exam.ExamRequestRepository
import com.cn.speaktest.repository.user.StudentRepository
import com.cn.speaktest.repository.user.UserRepository
import com.cn.speaktest.security.jwt.JwtUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/student")
@Validated
class StudentController(
    val jwtUtils: JwtUtils,
    val studentRepository: StudentRepository,
    val userRepository: UserRepository,
    val examRequestRepository: ExamRequestRepository,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun getProfile(@RequestHeader("Authorization") auth: String): Message {
        return StudentProfileResponse(getStudentByAuthToken(auth)).toOkMessage()
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
    ): Message {
        val student = getStudentByAuthToken(auth)

        if (fullName != null)
            student.fullName = fullName
        if (biography != null)
            student.biography = biography

        return StudentProfileResponse(
            studentRepository.save(student)
        ).toOkMessage()
    }

    @PostMapping("/exam-request")
    @PreAuthorize("hasRole('STUDENT')")
    fun examRequest(
        @RequestHeader("Authorization") auth: String
    ): Message {
        val student = getStudentByAuthToken(auth)

        if (examRequestRepository.existsByStudent(student))
            throw MethodNotAllowedException("You have an unhandled exam yet.")

        examRequestRepository.save(
            ExamRequest(
                Date(System.currentTimeMillis()),
                student
            )
        )
        return Message(null, "Your exam request submitted successfully")
    }

    private fun getStudentByAuthToken(auth: String): Student {
        val user = userRepository.findById(
            jwtUtils.getUserIdFromAuthorizationHeader(auth)
        ).orElseThrow { NotFoundException("User Not found") }
        return studentRepository.findByUser(user).orElseThrow { NotFoundException("Student Not found") }
    }
}