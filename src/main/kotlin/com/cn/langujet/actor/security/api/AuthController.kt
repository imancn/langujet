package com.cn.langujet.actor.security.api

import com.cn.langujet.actor.security.response.JwtResponse
import com.cn.langujet.actor.security.response.RefreshTokenResponse
import com.cn.langujet.application.arch.advice.InvalidCredentialException
import com.cn.langujet.application.arch.advice.InvalidInputException
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.controller.payload.response.MessageResponse
import com.cn.langujet.application.service.otp.OTP
import com.cn.langujet.application.service.otp.OTPService
import com.cn.langujet.application.service.smtp.MailSenderService
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.domain.corrector.CorrectorEntity
import com.cn.langujet.domain.corrector.CorrectorRepository
import com.cn.langujet.domain.corrector.CorrectorService
import com.cn.langujet.domain.student.model.StudentEntity
import com.cn.langujet.domain.student.service.StudentService
import com.cn.langujet.domain.user.model.Role
import com.cn.langujet.domain.user.model.UserDetailsImpl
import com.cn.langujet.domain.user.model.UserEntity
import com.cn.langujet.domain.user.repository.UserRepository
import com.cn.langujet.domain.user.services.GoogleAuthService
import com.cn.langujet.domain.user.services.JwtService
import com.cn.langujet.domain.user.services.UserService
import com.cn.langujet.domain.user.services.toStandardMail
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Value
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
    private val otpService: OTPService,
    private val mailSenderService: MailSenderService,
    private val encoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val googleAuthService: GoogleAuthService,
    private val studentService: StudentService,
    private val userService: UserService,
    private val correctorService: CorrectorService,
) {
//    @Todo: Move the logic to the service layer

    @Value("\${app.jwtRefreshExpirationMs}")
    private val jwtRefreshExpirationMs = 0L
    
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
        val refreshToken = generateRefreshToken(user)
        return ResponseEntity.ok(JwtResponse(jwt, refreshToken.token, userDetails.email))
    }

    private fun generateRefreshToken(user: UserEntity): OTP {
        return otpService.generate(
            key = OTP.Keys.REFRESH_TOKEN,
            userId = user.id(),
            ttl = jwtRefreshExpirationMs,
            len = 64,
            numeric = true,
            characters = true
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
        if (correctorRepository.existsByUser_Id(user.id())) {
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
        if (user.emailVerified) throw UnprocessableException("email.verification.already.verified")
        if (otpService.isValid(OTP.Keys.EMAIL_VERIFICATION, verificationCode, user.id())) {
            userService.save(user.also { it.emailVerified = true })
        } else {
            mailSenderService.sendOTP(
                otpService.generate(OTP.Keys.EMAIL_VERIFICATION, user.id()), user.email
            )
            throw UnprocessableException("email.verification.resend.code")
        }
        return ResponseEntity.ok("email.verification.successful")
    }
    
    @PostMapping("/signup/email/verification-mail")
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }

        if (user.emailVerified) throw UnprocessableException("email.verification.already.verified")

        val userId = user.id()
        val otp = otpService.findByUserId(OTP.Keys.EMAIL_VERIFICATION, userId) ?: otpService.generate(
            key = OTP.Keys.EMAIL_VERIFICATION,
            userId = userId
        )
        mailSenderService.sendOTP(otp, user.email)
        return ResponseEntity.ok("Verification Mail Has Been Sent.")
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestParam @NotBlank refreshToken: String,
    ): RefreshTokenResponse {
        val otp = otpService.findByToken(OTP.Keys.REFRESH_TOKEN, refreshToken)
            ?: throw UnprocessableException("invalid.refresh.token")
        val user = userService.getById(otp.userId)
        val token = jwtService.generateTokenFromUsername(user.username)
        val newOTP = otpService.generate(OTP.Keys.REFRESH_TOKEN, otp.userId)
        return RefreshTokenResponse(token, newOTP.token)
    }
    
    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndDeleted(email.toStandardMail())
            .orElseThrow { UnprocessableException("User Not Found") }
        val token = this.otpService.findByUserId(
            OTP.Keys.RESET_PASSWORD, user.id()
        ) ?: this.otpService.generate(
            OTP.Keys.RESET_PASSWORD, user.id()
        )
        mailSenderService.sendOTP(token, user.email)
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

        val token = this.otpService.findByUserId(OTP.Keys.RESET_PASSWORD, user.id())
            ?: throw InvalidCredentialException("Your reset password token has been expired. Request for reset password again.")

        if (token.token == code) {
            user.password = encoder.encode(newPassword)
            userService.save(user)
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
        otpService.findByUserId(OTP.Keys.REFRESH_TOKEN, Auth.userId())?.let { otp -> otpService.invalidate(otp) }
        return ResponseEntity.ok("User signed out successfully")
    }
    
    @PostMapping("/delete-account")
    @PreAuthorize("hasAnyRole('STUDENT')")
    fun deleteAccount(): MessageResponse {
        val user = userRepository.findByUsernameAndDeleted(Auth.username())
            .orElseThrow { UnprocessableException("User Not Found") }
        val otp = otpService.findOrGenerate(OTP.Keys.DELETE_ACCOUNT, user.id())
        mailSenderService.sendOTP(otp, user.email)
        return MessageResponse("successful", "Verification Mail Has Been Sent")
    }

    @PostMapping("/delete-account/verify")
    fun verifyDeleteAccount(@RequestParam @NotBlank verificationCode: String): MessageResponse {
        val user = userRepository.findByUsernameAndDeleted(Auth.email())
            .orElseThrow { UnprocessableException("User Not Found") }

        val otp = otpService.findByUserId(OTP.Keys.DELETE_ACCOUNT, user.id())
        if (otp == null) {
            mailSenderService.sendOTP(otpService.generate(OTP.Keys.DELETE_ACCOUNT, user.id()), user.email)
            throw UnprocessableException("Your verification code has been expired.\nWe sent a new verification code")
        } else if (otp.token != verificationCode) {
            throw InvalidInputException("Your verification code is not available")
        } else {
            userService.save(user.also { it.deleted = true })
            return MessageResponse("successful", "Your account has been deleted successfully")
        }
    }
}