package com.cn.speaktest.application.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MappingConfig { @Bean fun objectMapper() = ObjectMapper() }