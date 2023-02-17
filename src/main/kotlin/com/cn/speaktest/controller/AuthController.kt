package com.cn.speaktest.controller

import com.cn.speaktest.advice.*
import com.cn.speaktest.model.EmailVerificationToken
import com.cn.speaktest.model.Professor
import com.cn.speaktest.model.ResetPasswordToken
import com.cn.speaktest.model.Student
import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.model.security.Role
import com.cn.speaktest.model.security.User
import com.cn.speaktest.payload.request.auth.SignInRequest
import com.cn.speaktest.payload.request.auth.SignupRequest
import com.cn.speaktest.payload.request.auth.TokenRefreshRequest
import com.cn.speaktest.payload.response.user.JwtResponse
import com.cn.speaktest.payload.response.user.TokenRefreshResponse
import com.cn.speaktest.repository.user.*
import com.cn.speaktest.security.jwt.JwtUtils
import com.cn.speaktest.security.services.RefreshTokenService
import com.cn.speaktest.security.services.UserDetailsImpl
import com.cn.speaktest.service.MailSenderService
import jakarta.validation.Valid
import jakarta.validation.Validation
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@CrossOrigin(origins = ["*"], maxAge = 3600)
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
    val jwtUtils: JwtUtils
) {
    @PostMapping("/sign-in")
    fun authenticateUser(@Valid @RequestBody signInRequest: SignInRequest): Message {

        val user = userRepository.findByEmail(signInRequest.email).orElseThrow {
            InvalidTokenException("User Not Found")
        }

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user?.id, signInRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl

        if (!userDetails.emailVerified) throw MethodNotAllowedException("User is not enabled ${userDetails.email}")

        val roles = userDetails.authorities.stream().map { item: GrantedAuthority -> item.authority }
            .collect(Collectors.toList())

        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)

        return JwtResponse(
            jwt, refreshToken.token, userDetails.email
        ).toOkMessage()
    }

    @PostMapping("/signup/student")
    fun registerStudent(@Valid @RequestBody signUpRequest: SignupRequest): Message {
        val password = signUpRequest.password
        val email = signUpRequest.email

        val user = registerUser(email, password, mutableSetOf(Role.ROLE_STUDENT))

        sendVerificationMail(user.email)

        studentRepository.save(
            Student(
                user = user,
                fullName = signUpRequest.fullName
            )
        )
        return Message(null, "User registered successfully!")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup/professor")
    fun registerProfessor(@Valid @RequestBody signUpRequest: SignupRequest): Message {
        val password = signUpRequest.password
        val email = signUpRequest.email

        val user = registerUser(email, password, mutableSetOf(Role.ROLE_PROFESSOR))

        sendVerificationMail(email)

        professorRepository.save(
            Professor(
                user, signUpRequest.fullName
            )
        )
        return Message(null, "User registered successfully!")
    }

    private fun registerUser(email: String, password: String, roles: Set<Role>): User {
        if (userRepository.existsByEmail(email)) throw InvalidInputException("Email is already in use!")

        return userRepository.save(
            User(
                id = null,
                email = email,
                emailVerified = false,
                password = encoder.encode(password),
                roles = roles
            )
        )
    }

    @GetMapping("/signup/email/verify/{email}/{verificationCode}")
    fun verifyEmail(@PathVariable @Email @NotBlank email: String, @PathVariable verificationCode: String): Message {
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
    fun sendVerificationMail(@RequestParam @Email @NotBlank email: String): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }

        if (user.emailVerified) throw MethodNotAllowedException("Your Email Was Verified")

        val emailVerificationToken = emailVerificationTokenRepository.findByUser(user).orElse(
            emailVerificationTokenRepository.save(EmailVerificationToken(user))
        )

        mailSenderService.sendEmailVerificationMail(emailVerificationToken)

        return Message(null, "Verification Mail Has Been Sent.")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): Message {
        val refreshToken = refreshTokenService.findByToken(request.refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::user)
            .map { user ->
                val token = jwtUtils.generateTokenFromUserId(user.id)
                ResponseEntity.ok(TokenRefreshResponse(token, request.refreshToken))
            }
            .orElseThrow {
                RefreshTokenException("Refresh token [${request.refreshToken}] is not in database!")
            }
        return Message(refreshToken)
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam @Email @NotBlank email: String): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).orElse(
            resetPasswordTokenRepository.save(ResetPasswordToken(user))
        )
        mailSenderService.sendResetPasswordMail(token)
        return Message(null, "The reset password link has been mailed to you.")
    }

    @PostMapping("/reset-password/set")
    fun resetPassword(
        @RequestParam @Email @NotBlank email: String,
        @RequestParam code: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): Message {
        val user = userRepository.findByEmail(email).orElseThrow { NotFoundException("User Not Found") }
        val token = resetPasswordTokenRepository.findByUser(user).orElseThrow {
            InvalidTokenException("Your reset password token has been expired. Request for reset password again.")
        }

        if (token.token == code) {
            user.password = encoder.encode(newPassword)
            userRepository.save(user)
            resetPasswordTokenRepository.delete(token)
        } else
            throw InvalidTokenException("Your reset password token is invalid.")

        return Message(null, "Your password has been reset successfully")
    }

    @PostMapping("/change-password")
    fun changePassword(
        @RequestHeader("Authorization") auth: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank oldPassword: String,
        @RequestParam @Size(min = 6, max = 40) @NotBlank newPassword: String
    ): Message {
        val userId = jwtUtils.getUserIdFromAuthToken(auth)
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User Not Found") }
        if (user.password == encoder.encode(oldPassword))
            user.password = encoder.encode(newPassword)
        else throw InvalidInputException("Old Password is not correct.")
        return Message(null, "Your password has been changed.")
    }

    @PostMapping("/sign-out")
    fun signOutUser(@RequestHeader("Authorization") auth: String): Message {
        refreshTokenService.deleteByUserId(jwtUtils.getUserIdFromAuthToken(auth))
        return Message(null, "User signed out successfully")
    }
}