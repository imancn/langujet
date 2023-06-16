package com.cn.speaktest.actor.security.api

import com.cn.speaktest.application.advice.*
import com.cn.speaktest.application.security.security.model.*
import com.cn.speaktest.application.security.security.payload.response.JwtResponse
import com.cn.speaktest.application.security.security.payload.response.RefreshTokenResponse
import com.cn.speaktest.application.security.security.repository.EmailVerificationTokenRepository
import com.cn.speaktest.application.security.security.repository.ResetPasswordTokenRepository
import com.cn.speaktest.application.security.security.repository.UserRepository
import com.cn.speaktest.application.security.security.services.JwtService
import com.cn.speaktest.application.security.security.services.RefreshTokenService
import com.cn.speaktest.domain.security.services.AuthService
import com.cn.speaktest.application.thirdparty.smtp.MailSenderService
import com.cn.speaktest.domain.professor.Professor
import com.cn.speaktest.domain.professor.ProfessorRepository
import com.cn.speaktest.domain.student.model.Student
import com.cn.speaktest.domain.student.repository.StudentRepository
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/auth")
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
    @PostMapping("/sign-in")
    fun authenticateUser(
        @RequestParam @NotBlank @Email email: String?,
        @RequestParam @NotBlank password: String?,
    ): Message {
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

        return JwtResponse(
            jwt, refreshToken.token, userDetails.email
        ).toOkMessage()
    }

    @PostMapping("/signup/student")
    fun registerStudent(
        @RequestParam @NotBlank fullName: String?,
        @RequestParam @NotBlank @Size(max = 50) @Email email: String?,
        @RequestParam @NotBlank @Size(min = 6, max = 40) password: String?,
    ): Message {
        val user = registerUser(email!!, password!!, mutableSetOf(Role.ROLE_STUDENT))
        sendVerificationMail(user.email)
        studentRepository.save(Student(user, fullName))

        return Message(null, "User registered successfully!")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup/professor")
    fun registerProfessor(
        @NotBlank fullName: String?,
        @NotBlank @Size(max = 50) @Email email: String?,
        @NotBlank @Size(min = 6, max = 40) password: String?,
    ): Message {
        val user = registerUser(email!!, password!!, mutableSetOf(Role.ROLE_PROFESSOR))
        sendVerificationMail(email)
        professorRepository.save(Professor(user, fullName))

        return Message(null, "User registered successfully!")
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
    ): Message {
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

        return Message(null, "Email Verified successfully!")
    }

    @PostMapping("/signup/email/verification-mail")
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String?): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }

        if (user.emailVerified) throw MethodNotAllowedException("Your Email Was Verified")

        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenRepository.save(EmailVerificationToken(user))
        }

        mailSenderService.sendEmailVerificationMail(emailVerificationToken)

        return Message(null, "Verification Mail Has Been Sent.")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestParam @NotBlank refreshToken: String?): Message {
        return Message(
            refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::user).map { user ->
                    val token = jwtService.generateTokenFromUserId(user.id)
                    RefreshTokenResponse(token, refreshToken!!)
                }.orElseThrow {
                    RefreshTokenException("Refresh token [${refreshToken}] is not in database!")
                }
        )
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String?): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).getOrElse {
            resetPasswordTokenRepository.save(ResetPasswordToken(user))
        }
        mailSenderService.sendResetPasswordMail(token)
        return Message(null, "The reset password link has been mailed to you.")
    }

    @PostMapping("/reset-password/set")
    fun resetPassword(
        @RequestParam @Email @NotBlank email: String?,
        @RequestParam @NotBlank code: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String?
    ): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).orElseThrow {
            InvalidTokenException("Your reset password token has been expired. Request for reset password again.")
        }

        if (token.token == code) {
            user.password = encoder.encode(newPassword)
            userRepository.save(user)
            resetPasswordTokenRepository.delete(token)
        } else throw InvalidTokenException("Your reset password token is invalid.")

        return Message(null, "Your password has been reset successfully")
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    fun changePassword(
        @RequestHeader("Authorization") auth: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String?,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String?
    ): Message {
        val userId = authService.getUserIdFromAuthorizationHeader(auth)
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User Not Found") }

        if (user.password == encoder.encode(oldPassword)) user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")

        return Message(null, "Your password has been changed.")
    }

    @PostMapping("/sign-out")
    fun signOutUser(@RequestHeader("Authorization") auth: String?): Message {
        refreshTokenService.deleteByUserId(authService.getUserIdFromAuthorizationHeader(auth))
        return Message(null, "User signed out successfully")
    }
}