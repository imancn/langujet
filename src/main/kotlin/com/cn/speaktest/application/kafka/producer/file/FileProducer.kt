package com.cn.speaktest.application.kafka.producer.file

import com.cn.speaktest.application.file.domain.data.mongo.model.File
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class FileProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val mapper: ObjectMapper
) {

    @Value("\${spring.kafka.topics.file}")
    private lateinit var fileTopic: String
    fun sendFile(file: File) {
        kafkaTemplate.send(
            fileTopic,
            file.id,
            mapper.writeValueAsString(file))
    }
}