package com.cn.speaktest.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping("/all")
    fun allAccess(): String {
        return "Public Content."
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('PROFESSOR') or hasRole('ADMIN')")
    fun userAccess(): String {
        return "User Content."
    }

    @GetMapping("/professor")
    @PreAuthorize("hasRole('PROFESSOR')")
    fun professorAccess(): String {
        return "PROFESSOR Board."
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess(): String {
        return "Admin Board."
    }
}