package com.cn.langujet.application.config.security

import com.cn.langujet.domain.user.services.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    private val jwtService: JwtService,
    private val unauthorizedHandler: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
) {
    @Value("\${langujet.client.secret}")
    private lateinit var internalClientSecret: String
    
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/v3/api-docs/**").permitAll()
                authorize.requestMatchers("/swagger-ui.html").permitAll()
                authorize.requestMatchers("/swagger-ui/**").permitAll()

                authorize.requestMatchers("/error").permitAll()

                authorize.requestMatchers("/actuator/**").permitAll()

                authorize.requestMatchers("/api/*/public/**").permitAll()
                authorize.requestMatchers("/api/*/auth/**").permitAll()
                authorize.requestMatchers("/api/*/students/**").hasRole("STUDENTS")
                authorize.requestMatchers("/api/*/professors/**").hasRole("PROFESSORS")
                authorize.requestMatchers("/api/*/admin/**").hasRole("ADMIN")

                authorize.anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(AuthTokenFilter(jwtService, internalClientSecret), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
    
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().also { cors ->
        CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("POST", "GET")
            allowedHeaders = listOf(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )
            exposedHeaders = listOf(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"
            )
            maxAge = 3600
            cors.registerCorsConfiguration("/**", this)
        }
    }
}