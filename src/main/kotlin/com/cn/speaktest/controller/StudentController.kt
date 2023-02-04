package com.cn.speaktest.controller

import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.MethodNotAllowedException
import com.cn.speaktest.advice.NotFoundException
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.model.ExamRequest
import com.cn.speaktest.model.Student
import com.cn.speaktest.repository.exam.ExamRequestRepository
import com.cn.speaktest.repository.user.StudentRepository
import com.cn.speaktest.repository.user.UserRepository
import com.cn.speaktest.security.jwt.JwtUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/student")
class StudentController(
    val jwtUtils: JwtUtils,
    val studentRepository: StudentRepository,
    val userRepository: UserRepository,
    val examRequestRepository: ExamRequestRepository,
    val encoder: PasswordEncoder,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun getProfile(@RequestHeader("Authorization") auth: String): Message {
        return getStudentByAuthToken(auth).toOkMessage()
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
        @RequestParam email: String?,
        @RequestParam password: String?,
    ): Message {
        val student = getStudentByAuthToken(auth)

        if (fullName != null)
            student.fullName = fullName
        if (biography != null)
            student.biography = biography
        if (email != null)
            student.user.email = email
        if (password != null)
            student.user.password = encoder.encode(password)

        return studentRepository.save(student).toOkMessage()
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
            jwtUtils.getUserIdFromAuthToken(auth)
        ).orElseThrow { throw NotFoundException("User Not found") }
        return studentRepository.findByUser(user) ?: throw NotFoundException("Student Not found")
    }
}