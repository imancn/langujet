package com.cn.speaktest.controller

import com.cn.speaktest.advice.InvalidInputException
import com.cn.speaktest.advice.Message
import com.cn.speaktest.advice.RefreshTokenException
import com.cn.speaktest.advice.toOkMessage
import com.cn.speaktest.model.Professor
import com.cn.speaktest.model.Student
import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.model.security.Role
import com.cn.speaktest.model.security.User
import com.cn.speaktest.payload.request.auth.LoginRequest
import com.cn.speaktest.payload.request.auth.ProfessorSignupRequest
import com.cn.speaktest.payload.request.auth.StudentSignupRequest
import com.cn.speaktest.payload.request.auth.TokenRefreshRequest
import com.cn.speaktest.payload.response.user.JwtResponse
import com.cn.speaktest.payload.response.user.TokenRefreshResponse
import com.cn.speaktest.repository.user.ProfessorRepository
import com.cn.speaktest.repository.user.StudentRepository
import com.cn.speaktest.repository.user.UserRepository
import com.cn.speaktest.security.jwt.JwtUtils
import com.cn.speaktest.security.services.RefreshTokenService
import com.cn.speaktest.security.services.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val studentRepository: StudentRepository,
    val professorRepository: ProfessorRepository,
    val refreshTokenService: RefreshTokenService,
    val encoder: PasswordEncoder,
    val jwtUtils: JwtUtils
) {
    @PostMapping("/sign-in")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): Message {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl

        val roles = userDetails.authorities.stream()
            .map { item: GrantedAuthority -> item.authority }
            .collect(Collectors.toList())

        val refreshToken = refreshTokenService.createRefreshToken(userDetails.id)

        return JwtResponse(
            jwt,
            userDetails.id,
            userDetails.username,
            refreshToken.token,
            userDetails.email,
            roles
        ).toOkMessage()
    }

    @PostMapping("/signup/student")
    fun registerStudent(@RequestBody signUpRequest: @Valid StudentSignupRequest): Message {
        if (userRepository.existsByUsername(signUpRequest.username))
            throw InvalidInputException("Username is already taken!")

        if (userRepository.existsByEmail(signUpRequest.email))
            throw InvalidInputException("Email is already in use!")

        var user = User(
            id = null,
            username = signUpRequest.username ?: signUpRequest.email,
            email = signUpRequest.email,
            password = encoder.encode(signUpRequest.password)
        )
        val roles: MutableSet<Role> = HashSet()
        roles.add(
            Role.ROLE_STUDENT
        )
        user.roles = roles
        user = userRepository.save(user)

        studentRepository.save(
            Student(
                user = user,
                fullName = signUpRequest.fullName ?: signUpRequest.username ?: signUpRequest.email,
                biography = signUpRequest.biography
            )
        )
        return Message(null, "User registered successfully!")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup/professor")
    fun registerProfessor(@RequestBody signUpRequest: @Valid ProfessorSignupRequest): Message {
        if (userRepository.existsByUsername(signUpRequest.username))
            throw InvalidInputException("Username is already taken!")

        if (userRepository.existsByEmail(signUpRequest.email))
            throw InvalidInputException("Email is already in use!")

        var user = User(
            id = null,
            username = signUpRequest.username,
            email = signUpRequest.email,
            password = encoder.encode(signUpRequest.password)
        )
        val roles: MutableSet<Role> = HashSet()
        roles.add(
            Role.ROLE_PROFESSOR
        )
        user.roles = roles
        user = userRepository.save(user)
        professorRepository.save(
            Professor(
                user,
                signUpRequest.fullName,
                signUpRequest.biography,
                signUpRequest.ieltsScore
            )
        )
        return Message(null, "User registered successfully!")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): Message {
        val refreshToken = refreshTokenService.findByToken(request.refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::user)
            .map { user ->
                val token = jwtUtils.generateTokenFromUsername(user.username)
                ResponseEntity.ok(TokenRefreshResponse(token, request.refreshToken))
            }
            .orElseThrow {
                RefreshTokenException("Refresh token [${request.refreshToken}] is not in database!")
            }
        return Message(refreshToken)
    }

    @PostMapping("/logout")
    fun logoutUser(@RequestHeader("Authorization") auth: String): Message {
        refreshTokenService.deleteByUsername(jwtUtils.getUserNameFromAuthToken(auth))
        return Message(null, "Log out successful!")
    }
}