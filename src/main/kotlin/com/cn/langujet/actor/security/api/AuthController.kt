package com.cn.langujet.actor.security.api

import com.cn.langujet.actor.util.Auth
import com.cn.langujet.actor.util.toOkResponseEntity
import com.cn.langujet.application.advice.*
import com.cn.langujet.actor.security.response.JwtResponse
import com.cn.langujet.actor.security.response.RefreshTokenResponse
import com.cn.langujet.application.service.smtp.MailSenderService
import com.cn.langujet.domain.corrector.CorrectorEntity
import com.cn.langujet.domain.corrector.CorrectorRepository
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.repository.StudentRepository
import com.cn.langujet.domain.user.model.*
import com.cn.langujet.domain.user.repository.EmailVerificationTokenRepository
import com.cn.langujet.domain.user.repository.ResetPasswordTokenRepository
import com.cn.langujet.domain.user.repository.UserRepository
import com.cn.langujet.domain.user.services.GoogleAuthService
import com.cn.langujet.domain.user.services.JwtService
import com.cn.langujet.domain.user.services.RefreshTokenService
import com.cn.langujet.domain.user.services.toStandardMail
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
    val correctorRepository: CorrectorRepository,
    val refreshTokenService: RefreshTokenService,
    val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    val resetPasswordTokenRepository: ResetPasswordTokenRepository,
    val mailSenderService: MailSenderService,
    val encoder: PasswordEncoder,
    val jwtService: JwtService,
    private val googleAuthService: GoogleAuthService,
) {
//    @Todo: Move the logic to the service layer
    
    @PostMapping("/sign-in")
    fun authenticateUser(
        @RequestParam @NotBlank @Email email: String,
        @RequestParam @NotBlank password: String,
    ): ResponseEntity<JwtResponse> {
        val user = userRepository.findByStandardEmailAndDeleted(email.toStandardMail()).orElseThrow {
            AccessDeniedException("Invalid credentials")
        }
        if (user.password.isNullOrBlank()) throw UnprocessableException("You must reset your password")
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.id, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtToken(authentication.principal as UserDetailsImpl)
        val userDetails = authentication.principal as UserDetailsImpl
        if (!userDetails.emailVerified) throw UnprocessableException("Your email isn't verified")
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return toOkResponseEntity(
            JwtResponse(
                jwt, refreshToken.id ?: "", userDetails.email
            )
        )
    }
    
    @PostMapping("/google")
    fun googleAuth(@RequestParam @NotBlank authCode: String): JwtResponse {
        throw UnprocessableException("Temporary unavailable")
        return googleAuthService.authenticateWithGoogle(authCode)
    }
    
    @PostMapping("/student/signup")
    fun registerStudent(
        @RequestParam @NotBlank fullName: String,
        @RequestParam @NotBlank @Size(max = 50) @Email email: String,
        @RequestParam @NotBlank @Size(min = 6, max = 40) password: String,
    ): ResponseEntity<String> {
        val user = registerUser(email.toStandardMail(), password, mutableSetOf(Role.ROLE_STUDENT))
        sendVerificationMail(user.email)
        studentRepository.save(StudentEntity(user, fullName))
        return toOkResponseEntity("User registered successfully!")
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/corrector/signup")
    fun registerCorrector(
        @RequestParam @NotBlank fullName: String,
        @RequestParam @NotBlank @Email email: String,
        @RequestParam @NotBlank @Size(min = 6, max = 40) password: String,
    ): ResponseEntity<String> {
        val user = registerUser(email.toStandardMail(), password, mutableSetOf(Role.ROLE_CORRECTOR))
        sendVerificationMail(email.toStandardMail())
        correctorRepository.save(CorrectorEntity(user, fullName))
        return toOkResponseEntity("User registered successfully!")
    }
    
    private fun registerUser(email: String, password: String, roles: Set<Role>): UserEntity {
        if (userRepository.existsByStandardEmailAndDeleted(email.toStandardMail())) throw InvalidInputException("Email is already in use!")
        return userRepository.save(
            UserEntity(
                id = null,
                standardEmail = email.toStandardMail(),
                email = email,
                emailVerified = false,
                password = encoder.encode(password),
                roles = roles
            )
        )
    }
    
    @GetMapping("/signup/email/verify/{email}/{verificationCode}")
    fun verifyEmail(
        @PathVariable @Email @NotBlank email: String, @PathVariable @NotBlank verificationCode: String
    ): ResponseEntity<String> {
        val user = userRepository.findByStandardEmailAndDeleted(email.toStandardMail())
            .orElseThrow { NotFoundException("User Not Found") }
        if (user.emailVerified) throw UnprocessableException("Your Email Was Verified")
        val verificationToken = emailVerificationTokenRepository.findByUser(user).orElseThrow {
            mailSenderService.sendEmailVerificationMail(
                emailVerificationTokenRepository.save(EmailVerificationTokenEntity(user))
            )
            UnprocessableException("Your verification code has been expired.\nWe sent a new verification code.")
        }
        if (verificationToken.token == verificationCode) userRepository.save(user.also { it.emailVerified = true })
        else throw InvalidInputException("Your verification code is not available.")
        return toOkResponseEntity("Email Verified successfully!")
    }
    
    @PostMapping("/signup/email/verification-mail")
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByStandardEmailAndDeleted(email.toStandardMail())
            .orElseThrow { NotFoundException("User Not Found") }
        
        if (user.emailVerified) throw UnprocessableException("Your Email Was Verified")
        
        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenRepository.save(EmailVerificationTokenEntity(user))
        }
        mailSenderService.sendEmailVerificationMail(emailVerificationToken)
        return toOkResponseEntity("Verification Mail Has Been Sent.")
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam @NotBlank refreshToken: String,
    ): ResponseEntity<RefreshTokenResponse> {
        return toOkResponseEntity(refreshTokenService.findByToken(refreshToken).map {
            val token = jwtService.generateTokenFromUserId(it.userId)
            val newRefreshToken = refreshTokenService.createRefreshToken(it.userId)
            refreshTokenService.deleteById(refreshToken)
            RefreshTokenResponse(token, newRefreshToken.id ?: "")
        }.orElseThrow {
            RefreshTokenException("Your Session has been expired")
        })
    }
    
    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByStandardEmailAndDeleted(email.toStandardMail())
            .orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).getOrElse {
            resetPasswordTokenRepository.save(ResetPasswordTokenEntity(user))
        }
        mailSenderService.sendResetPasswordMail(token)
        return toOkResponseEntity("The reset password link has been mailed to you.")
    }
    
    @PostMapping("/reset-password/set")
    fun resetPassword(
        @RequestParam @Email @NotBlank email: String,
        @RequestParam @NotBlank code: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): ResponseEntity<String> {
        val user = userRepository.findByStandardEmailAndDeleted(email.toStandardMail())
            .orElseThrow { NotFoundException("User Not Found") }
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    fun changePassword(
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): ResponseEntity<String> {
        val user = userRepository.findByIdAndDeleted(Auth.userId()).orElseThrow { NotFoundException("User Not Found") }
        if (user.password.isNullOrBlank()) throw UnprocessableException("You must reset your password")
        if (encoder.matches(oldPassword, user.password)) user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")
        return toOkResponseEntity("Your password has been changed.")
    }
    
    @PostMapping("/sign-out")
    fun signOutUser(): ResponseEntity<String> {
        refreshTokenService.deleteByUserId(Auth.userId())
        return toOkResponseEntity("User signed out successfully")
    }
    
    @PostMapping("/delete-account")
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun deleteAccount(): String {
        val user = userRepository.findByStandardEmailAndDeleted(Auth.userEmail())
            .orElseThrow { UnprocessableException("User Not Found") }
        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenRepository.save(EmailVerificationTokenEntity(user))
        }
        mailSenderService.sendDeleteAccountVerificationMail(emailVerificationToken)
        return "Verification Mail Has Been Sent"
    }
    
    @PostMapping("/delete-account/verify")
    fun verifyDeleteAccount(@RequestParam @NotBlank verificationCode: String): String {
        val user = userRepository.findByStandardEmailAndDeleted(Auth.userEmail())
            .orElseThrow { NotFoundException("User Not Found") }
        val verificationToken = emailVerificationTokenRepository.findByUser(user).orElseThrow {
            mailSenderService.sendDeleteAccountVerificationMail(
                emailVerificationTokenRepository.save(EmailVerificationTokenEntity(user))
            )
            UnprocessableException("Your verification code has been expired.\nWe sent a new verification code")
        }
        if (verificationToken.token == verificationCode) userRepository.save(user.also { it.deleted = true })
        else throw InvalidInputException("Your verification code is not available")
        return "Your account has been deleted successfully"
    }
}