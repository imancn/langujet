package com.cn.speaktest.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition
@Configuration
class OpenApiConfig {

    @Bean
    fun baseOpenApi(): OpenAPI {
        return OpenAPI()
            .info(Info().title("API Documentation"))
    }
}