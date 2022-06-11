package com.cn.speaktest.controller

import com.cn.speaktest.model.Student
import com.cn.speaktest.payload.response.MessageResponse
import com.cn.speaktest.repository.StudentRepository
import com.cn.speaktest.repository.UserRepository
import com.cn.speaktest.security.jwt.JwtUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/student")
class StudentController(
    val jwtUtils: JwtUtils,
    val studentRepository: StudentRepository,
    val userRepository: UserRepository,
    val encoder: PasswordEncoder,
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun getProfile(@RequestHeader("Authorization") auth: String): ResponseEntity<Student> {
        return ResponseEntity.ok(getStudentByAuthToken(auth))
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    fun editProfile(
        @RequestHeader("Authorization") auth: String,
        @RequestParam fullName: String?,
        @RequestParam biography: String?,
        @RequestParam email: String?,
        @RequestParam password: String?,
    ): ResponseEntity<*> {
        val student = getStudentByAuthToken(auth)
        student ?: return ResponseEntity.status(404).body(MessageResponse("User Not found"))

        if (fullName != null)
            student.fullName = fullName
        if (biography != null)
            student.biography = biography
        if (email != null)
            student.user.email = email
        if (password != null)
            student.user.password = encoder.encode(password)

        studentRepository.save(student)

        return ResponseEntity.ok(MessageResponse("Profile updated successfully"))
    }

    private fun getStudentByAuthToken(auth: String): Student? {
        val user = userRepository.findByUsername(jwtUtils.getUserNameFromAuthToken(auth))
        return studentRepository.findByUser(
            user ?: return null
        )
    }
}