package com.cn.speaktest.controller

import com.cn.speaktest.exception.TokenRefreshException
import com.cn.speaktest.model.ERole
import com.cn.speaktest.model.RefreshToken
import com.cn.speaktest.model.Role
import com.cn.speaktest.model.User
import com.cn.speaktest.payload.request.LoginRequest
import com.cn.speaktest.payload.request.SignupRequest
import com.cn.speaktest.payload.request.TokenRefreshRequest
import com.cn.speaktest.payload.response.JwtResponse
import com.cn.speaktest.payload.response.MessageResponse
import com.cn.speaktest.payload.response.TokenRefreshResponse
import com.cn.speaktest.repository.RoleRepository
import com.cn.speaktest.repository.UserRepository
import com.cn.speaktest.security.jwt.JwtUtils
import com.cn.speaktest.security.services.RefreshTokenService
import com.cn.speaktest.security.services.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
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

    @PostMapping("/sign-up")
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest): ResponseEntity<*> {
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

        // Create new user's account
        val user = User(
            null,
            signUpRequest.username,
            signUpRequest.email,
            encoder.encode(signUpRequest.password)
        )
        val strRoles = signUpRequest.roles
        val roles: MutableSet<Role> = HashSet()
        if (strRoles == null) {
            val userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow { RuntimeException("Error: Role is not found.") }
            roles.add(userRole)
        } else {
            strRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(adminRole)
                    }
                    "professor" -> {
                        val professorRole = roleRepository.findByName(ERole.ROLE_PROFESSOR)
                            .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(professorRole)
                    }
                    else -> {
                        val userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow { RuntimeException("Error: Role is not found.") }
                        roles.add(userRole)
                    }
                }
            })
        }
        user.roles = roles
        userRepository.save(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    @PostMapping("/refresh-token")
    open fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<*>? {
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
        val token = if (StringUtils.hasText(auth) && auth.startsWith("Bearer "))
            auth.substring(7, auth.length)
        else null

        refreshTokenService.deleteByUsername(jwtUtils.getUserNameFromJwtToken(token))

        return ResponseEntity.ok<Any>(MessageResponse("Log out successful!"))
    }
}