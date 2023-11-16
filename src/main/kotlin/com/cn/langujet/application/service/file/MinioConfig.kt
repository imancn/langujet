package com.cn.langujet.application.service.file

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class MinioConfig(
    @Value("\${minio.url}") val url: String?,
    @Value("\${minio.access-key}") val accessKey: String?,
    @Value("\${minio.secret-key}") val secretKey: String?,
) {
    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build()
    }
}