package com.cn.speaktest.controller

import com.cn.speaktest.model.Professor
import com.cn.speaktest.model.Student
import com.cn.speaktest.model.security.ERole
import com.cn.speaktest.model.security.RefreshToken
import com.cn.speaktest.model.security.Role
import com.cn.speaktest.model.security.User
import com.cn.speaktest.payload.request.LoginRequest
import com.cn.speaktest.payload.request.ProfessorSignupRequest
import com.cn.speaktest.payload.request.StudentSignupRequest
import com.cn.speaktest.payload.request.TokenRefreshRequest
import com.cn.speaktest.payload.response.JwtResponse
import com.cn.speaktest.payload.response.MessageResponse
import com.cn.speaktest.payload.response.TokenRefreshResponse
import com.cn.speaktest.repository.ProfessorRepository
import com.cn.speaktest.repository.RoleRepository
import com.cn.speaktest.repository.StudentRepository
import com.cn.speaktest.repository.UserRepository
import com.cn.speaktest.security.exception.TokenRefreshException
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
    val roleRepository: RoleRepository,
    val studentRepository: StudentRepository,
    val professorRepository: ProfessorRepository,
    val refreshTokenService: RefreshTokenService,
    val encoder: PasswordEncoder,
    val jwtUtils: JwtUtils
) {
    @PostMapping("/sign-in")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {
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

        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                userDetails.id,
                userDetails.username,
                refreshToken.token,
                userDetails.email,
                roles
            )
        )
    }

    @PostMapping("/signup/student")
    fun registerStudent(@RequestBody signUpRequest: @Valid StudentSignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username) == true) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository.existsByEmail(signUpRequest.email) == true) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Email is already in use!"))
        }
        var user = User(
            null,
            signUpRequest.username,
            signUpRequest.email,
            encoder.encode(signUpRequest.password)
        )
        val roles: MutableSet<Role> = HashSet()
        roles.add(
            roleRepository.findByName(ERole.ROLE_STUDENT)
                .orElseThrow { RuntimeException("Error: Role is not found.") }
        )
        user.roles = roles
        user = userRepository.save(user)
        studentRepository.save(
            Student(
                user,
                signUpRequest.fullName,
                signUpRequest.biography
            )
        )
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup/professor")
    fun registerProfessor(@RequestBody signUpRequest: @Valid ProfessorSignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username) == true) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository.existsByEmail(signUpRequest.email) == true) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Email is already in use!"))
        }
        var user = User(
            null,
            signUpRequest.username,
            signUpRequest.email,
            encoder.encode(signUpRequest.password)
        )
        val roles: MutableSet<Role> = HashSet()
        roles.add(
            roleRepository.findByName(ERole.ROLE_PROFESSOR)
                .orElseThrow { RuntimeException("Error: Role is not found.") }
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
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<*>? {
        val requestRefreshToken: String = request.refreshToken
        return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::user)
            .map { user ->
                val token = jwtUtils.generateTokenFromUsername(user.username)
                ResponseEntity.ok(TokenRefreshResponse(token, requestRefreshToken))
            }
            .orElseThrow {
                TokenRefreshException(
                    requestRefreshToken,
                    "Refresh token is not in database!"
                )
            }
    }

    @PostMapping("/logout")
    fun logoutUser(@RequestHeader("Authorization") auth: String): ResponseEntity<*>? {
        refreshTokenService.deleteByUsername(jwtUtils.getUserNameFromAuthToken(auth))
        return ResponseEntity.ok<Any>(MessageResponse("Log out successful!"))
    }
}