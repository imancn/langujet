package com.cn.langujet.actor.security.api

import com.cn.langujet.actor.security.response.JwtResponse
import com.cn.langujet.actor.security.response.RefreshTokenResponse
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.service.smtp.MailSenderService
import com.cn.langujet.domain.corrector.CorrectorEntity
import com.cn.langujet.domain.corrector.CorrectorRepository
import com.cn.langujet.domain.corrector.CorrectorService
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.service.StudentService
import com.cn.langujet.domain.user.model.*
import com.cn.langujet.domain.user.repository.EmailVerificationTokenRepository
import com.cn.langujet.domain.user.repository.ResetPasswordTokenRepository
import com.cn.langujet.domain.user.repository.UserRepository
import com.cn.langujet.domain.user.services.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
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
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/v1/auth")
@Validated
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val correctorRepository: CorrectorRepository,
    private val refreshTokenService: RefreshTokenService,
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    private val resetPasswordTokenRepository: ResetPasswordTokenRepository,
    private val mailSenderService: MailSenderService,
    private val encoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val googleAuthService: GoogleAuthService,
    private val studentService: StudentService,
    private val userService: UserService,
    private val correctorService: CorrectorService,
    private val emailVerificationTokenService: EmailVerificationTokenService,
    private val resetPasswordTokenService: ResetPasswordTokenService,
) {
//    @Todo: Move the logic to the service layer
    
    @PostMapping("/sign-in/mail")
    fun signingByEmail(
        @RequestParam @NotBlank @Email email: String,
        @RequestParam @NotBlank password: String,
    ): ResponseEntity<JwtResponse> {
        val user = userRepository.findByEmailAndDeleted(email).getOrNull()
        return user?.username?.let { signIn(it, password) } ?: signIn(email.toStandardMail(), password)
    }
    
    @PostMapping("/sign-in/username")
    fun signingByUsername(
        @RequestParam @NotBlank username: String,
        @RequestParam @NotBlank password: String,
    ): ResponseEntity<JwtResponse> {
        return signingByEmail(username, password)
    }
    
    private fun signIn(username: String, password: String): ResponseEntity<JwtResponse> {
        val user = userRepository.findByUsernameAndDeleted(username).orElseThrow {
            InvalidCredentialException("Invalid credentials")
        }
        if (user.password.isNullOrBlank()) throw UnprocessableException("You must reset your password")
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.username, password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtService.generateJwtToken(authentication.principal as UserDetailsImpl)
        val userDetails = authentication.principal as UserDetailsImpl
        if (!userDetails.emailVerified) throw UnprocessableException("Your email isn't verified")
        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)
        return ResponseEntity.ok(
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
        val user = registerUserByEmail(email, password, mutableSetOf(Role.ROLE_STUDENT))
        sendVerificationMail(user.email)
        studentService.create(StudentEntity(user, fullName))
        return ResponseEntity.ok("User registered successfully!")
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/corrector/signup")
    fun registerCorrector(
        @RequestParam @NotBlank @Email email: String,
        @RequestParam @NotNull ieltsScore: Double,
    ): ResponseEntity<String> {
        var user = userRepository.findByUsernameAndDeleted(email.toStandardMail()).getOrElse {
            throw UnprocessableException("user must sign-up as student first")
        }
        if (user.id?.let { correctorRepository.existsByUser_Id(it) } != false){
            throw UnprocessableException("Corrector already registered")
        }
        val fullName = user.id?.let { studentService.getStudentByUserId(it) }?.fullName ?: user.email
        user.roles =  user.roles.toMutableSet().also { it.add(Role.ROLE_CORRECTOR) }
        user = userService.save(user)
        correctorService.save(CorrectorEntity(user, fullName, ieltsScore))
        return ResponseEntity.ok("Corrector registered successfully!")
    }
    
    private fun registerUserByEmail(email: String, password: String, roles: Set<Role>): UserEntity {
        if (userRepository.existsByUsernameAndDeleted(email.toStandardMail())) throw InvalidInputException("Email is already in use!")
        return userService.save(
            UserEntity(
                id = null,
                username = email.toStandardMail(),
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
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }
        if (user.emailVerified) throw UnprocessableException("Your Email Was Verified")
        val verificationToken = emailVerificationTokenRepository.findByUser(user).orElseThrow {
            mailSenderService.sendEmailVerificationMail(
                emailVerificationTokenService.save(EmailVerificationTokenEntity(user))
            )
            UnprocessableException("Your verification code has been expired.\nWe sent a new verification code.")
        }
        if (verificationToken.token == verificationCode) userService.save(user.also { it.emailVerified = true })
        else throw InvalidInputException("Your verification code is not available.")
        return ResponseEntity.ok("Email Verified successfully!")
    }
    
    @PostMapping("/signup/email/verification-mail")
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }
        
        if (user.emailVerified) throw UnprocessableException("Your Email Was Verified")
        
        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenService.save(EmailVerificationTokenEntity(user))
        }
        mailSenderService.sendEmailVerificationMail(emailVerificationToken)
        return ResponseEntity.ok("Verification Mail Has Been Sent.")
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam @NotBlank refreshToken: String,
    ): ResponseEntity<RefreshTokenResponse> {
        return ResponseEntity.ok(refreshTokenService.findByToken(refreshToken).map { refreshTokenEntity ->
            val user = userService.getById(refreshTokenEntity.userId)
            val token = jwtService.generateTokenFromUsername(user.username)
            val newRefreshToken = refreshTokenService.createRefreshToken(refreshTokenEntity.userId)
            refreshTokenService.deleteById(refreshToken)
            RefreshTokenResponse(token, newRefreshToken.id ?: "")
        }.orElseThrow {
            InvalidCredentialException("Your Session has been expired")
        })
    }
    
    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).getOrElse {
            resetPasswordTokenService.save(ResetPasswordTokenEntity(user))
        }
        mailSenderService.sendResetPasswordMail(token)
        return ResponseEntity.ok("The reset password link has been mailed to you.")
    }
    
    @PostMapping("/reset-password/set")
    fun resetPassword(
        @RequestParam @Email @NotBlank email: String,
        @RequestParam @NotBlank code: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).orElseThrow {
            InvalidCredentialException("Your reset password token has been expired. Request for reset password again.")
        }
        if (token.token == code) {
            user.password = encoder.encode(newPassword)
            userService.save(user)
            resetPasswordTokenRepository.delete(token)
        } else throw InvalidCredentialException("Your reset password token is invalid.")
        return ResponseEntity.ok("Your password has been reset successfully")
    }
    
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    fun changePassword(
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): ResponseEntity<String> {
        val user = userRepository.findByIdAndDeleted(Auth.userId()).orElseThrow { UnprocessableException("User Not Found") }
        if (user.password.isNullOrBlank()) throw UnprocessableException("You must reset your password")
        if (encoder.matches(oldPassword, user.password)) user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")
        return ResponseEntity.ok("Your password has been changed.")
    }
    
    @PostMapping("/sign-out")
    fun signOutUser(): ResponseEntity<String> {
        refreshTokenService.deleteByUserId(Auth.userId())
        return ResponseEntity.ok("User signed out successfully")
    }
    
    @PostMapping("/delete-account")
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun deleteAccount(): String {
        val user = userRepository.findByUsernameAndDeleted(Auth.email())
            .orElseThrow { UnprocessableException("User Not Found") }
        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).getOrElse {
            emailVerificationTokenService.save(EmailVerificationTokenEntity(user))
        }
        mailSenderService.sendDeleteAccountVerificationMail(emailVerificationToken)
        return "Verification Mail Has Been Sent"
    }
    
    @PostMapping("/delete-account/verify")
    fun verifyDeleteAccount(@RequestParam @NotBlank verificationCode: String): String {
        val user = userRepository.findByUsernameAndDeleted(Auth.email())
            .orElseThrow { UnprocessableException("User Not Found") }
        val verificationToken = emailVerificationTokenRepository.findByUser(user).orElseThrow {
            mailSenderService.sendDeleteAccountVerificationMail(
                emailVerificationTokenService.save(EmailVerificationTokenEntity(user))
            )
            UnprocessableException("Your verification code has been expired.\nWe sent a new verification code")
        }
        if (verificationToken.token == verificationCode) userService.save(user.also { it.deleted = true })
        else throw InvalidInputException("Your verification code is not available")
        return "Your account has been deleted successfully"
    }
}