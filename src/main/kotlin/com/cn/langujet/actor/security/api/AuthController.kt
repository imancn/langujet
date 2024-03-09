package com.cn.langujet.actor.security.api

import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.*
import com.cn.langujet.application.security.security.model.*
import com.cn.langujet.application.security.security.payload.response.JwtResponse
import com.cn.langujet.application.security.security.payload.response.RefreshTokenResponse
import com.cn.langujet.domain.security.repository.EmailVerificationTokenRepository
import com.cn.langujet.domain.security.repository.ResetPasswordTokenRepository
import com.cn.langujet.domain.security.repository.UserRepository
import com.cn.langujet.domain.security.services.JwtService
import com.cn.langujet.domain.security.services.RefreshTokenService
import com.cn.langujet.application.service.smtp.MailSenderService
import com.cn.langujet.domain.professor.Professor
import com.cn.langujet.domain.professor.ProfessorRepository
import com.cn.langujet.domain.security.model.*
import com.cn.langujet.domain.security.services.AuthService
import com.cn.langujet.domain.student.model.Student
import com.cn.langujet.domain.student.repository.StudentRepository
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/v1/auth")
@Validated
class AuthController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val studentRepository: StudentRepository,
    val professorRepository: ProfessorRepository,
    val refreshTokenService: RefreshTokenService,
    val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    val resetPasswordTokenRepository: ResetPasswordTokenRepository,
    val mailSenderService: MailSenderService,
    val encoder: PasswordEncoder,
    val jwtService: JwtService,
    val authService: AuthService
) {
//    @Todo: Move the logic to the service layer

    @PostMapping("/sign-in")
    fun authenticateUser(
        @RequestParam @NotBlank @Email email: String?,
        @RequestParam @NotBlank password: String?,
    ): ResponseEntity<JwtResponse> {
        val user = userRepository.findByEmail(email).orElseThrow {
            InvalidTokenException("Bad credentials")
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user?.id, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        if (!userDetails.emailVerified) throw MethodNotAllowedException("User is not enabled ${userDetails.email}")

        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)

        return toOkResponseEntity(
            JwtResponse(
                jwt, refreshToken.id ?: "", userDetails.email
            )
        )
    }
    
    // Todo: remove after test
    @PostMapping("/sign-in/test")
    fun authenticateUserTest(
        @RequestParam @NotBlank @Email email: String?,
        @RequestParam @NotBlank password: String?,
    ): ResponseEntity<JwtResponse> {
        val user = userRepository.findByEmail(email).orElseThrow {
            InvalidTokenException("Bad credentials")
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user?.id, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtTokenTest(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        if (!userDetails.emailVerified) throw MethodNotAllowedException("User is not enabled ${userDetails.email}")
        
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        
        return toOkResponseEntity(
            JwtResponse(
                jwt, refreshToken.id ?: "", userDetails.email
            )
        )
    }

    @PostMapping("/student/signup")
    fun registerStudent(
        @RequestParam @NotBlank fullName: String?,
        @RequestParam @NotBlank @Size(max = 50) @Email email: String?,
        @RequestParam @NotBlank @Size(min = 6, max = 40) password: String?,
    ): ResponseEntity<String> {
        val user = registerUser(email!!, password!!, mutableSetOf(Role.ROLE_STUDENT))
        sendVerificationMail(user.email)
        studentRepository.save(Student(user, fullName))

        return toOkResponseEntity("User registered successfully!")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/professor/signup")
    fun registerProfessor(
        @NotBlank fullName: String?,
        @NotBlank @Size(max = 50) @Email email: String?,
        @NotBlank @Size(min = 6, max = 40) password: String?,
    ): ResponseEntity<String> {
        val user = registerUser(email!!, password!!, mutableSetOf(Role.ROLE_PROFESSOR))
        sendVerificationMail(email)
        professorRepository.save(Professor(user, fullName))

        return toOkResponseEntity("User registered successfully!")
    }

    private fun registerUser(email: String, password: String, roles: Set<Role>): User {
        if (userRepository.existsByEmail(email)) throw InvalidInputException("Email is already in use!")

        return userRepository.save(
            User(
                id = null, email = email, emailVerified = false, password = encoder.encode(password), roles = roles
            )
        )
    }

    @GetMapping("/signup/email/verify/{email}/{verificationCode}")
    fun verifyEmail(
        @PathVariable @Email @NotBlank email: String?,
        @PathVariable @NotBlank verificationCode: String?
    ): ResponseEntity<String> {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }

        if (user.emailVerified) throw MethodNotAllowedException("Your Email Was Verified")

        val verificationToken = emailVerificationTokenRepository.findByUser(user).orElseThrow {
            mailSenderService.sendEmailVerificationMail(
                emailVerificationTokenRepository.save(EmailVerificationToken(user))
            )
            MethodNotAllowedException("Your verification code has been expired.\nWe sent a new verification code.")
        }

        if (verificationToken.token == verificationCode) userRepository.save(user.also { it.emailVerified = true })
        else throw InvalidInputException("Your verification code is not available.")

        return toOkResponseEntity("Email Verified successfully!")
    }

    @PostMapping("/signup/email/verification-mail")
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String?): ResponseEntity<String> {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }

        if (user.emailVerified) throw MethodNotAllowedException("Your Email Was Verified")

        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenRepository.save(EmailVerificationToken(user))
        }

        mailSenderService.sendEmailVerificationMail(emailVerificationToken)

        return toOkResponseEntity("Verification Mail Has Been Sent.")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam @NotBlank refreshToken: String?,
    ): ResponseEntity<RefreshTokenResponse> {
        return toOkResponseEntity(
            refreshTokenService.findByToken(refreshToken!!)
                .map {
                    val token = jwtService.generateTokenFromUserId(it.userId)
                    val newRefreshToken = refreshTokenService.createRefreshToken(it.userId)
                    RefreshTokenResponse(token, newRefreshToken.id ?: "")
                }.orElseThrow {
                    RefreshTokenException("Refresh token [${refreshToken}] is not available")
                }
        )
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String?): ResponseEntity<String> {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).getOrElse {
            resetPasswordTokenRepository.save(ResetPasswordToken(user))
        }
        mailSenderService.sendResetPasswordMail(token)
        return toOkResponseEntity("The reset password link has been mailed to you.")
    }

    @PostMapping("/reset-password/set")
    fun resetPassword(
        @RequestParam @Email @NotBlank email: String?,
        @RequestParam @NotBlank code: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String?
    ): ResponseEntity<String> {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).orElseThrow {
            InvalidTokenException("Your reset password token has been expired. Request for reset password again.")
        }

        if (token.token == code) {
            user.password = encoder.encode(newPassword)
            userRepository.save(user)
            resetPasswordTokenRepository.delete(token)
        } else throw InvalidTokenException("Your reset password token is invalid.")

        return toOkResponseEntity("Your password has been reset successfully")
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    fun changePassword(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String?
    ): ResponseEntity<String> {
        val userId = authService.getUserIdFromAuthorizationHeader(auth)
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User Not Found") }

        if (user.password == encoder.encode(oldPassword)) user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")

        return toOkResponseEntity("Your password has been changed.")
    }

    @PostMapping("/sign-out")
    fun signOutUser(@RequestHeader("Authorization") auth: String?): ResponseEntity<String> {
        refreshTokenService.deleteByUserId(authService.getUserIdFromAuthorizationHeader(auth))
        return toOkResponseEntity("User signed out successfully")
    }
}