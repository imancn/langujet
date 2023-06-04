package com.cn.speaktest.application.kafka.consumer.file

import com.cn.speaktest.application.file.domain.data.mongo.model.File
import com.cn.speaktest.application.file.domain.service.FileService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class FileConsumer(
    private val fileService: FileService,
    private val mapper: ObjectMapper
) {
    @KafkaListener(topics = ["files"])
    fun receive(records: List<String>, ack: Acknowledgment) {
        if (handle(records)) ack.acknowledge()
    }

    private fun handle(records: List<String>): Boolean {
        records.mapNotNull { json ->
            mapper.readValue(json, File::class.java)
        }.parallelStream().forEach { file ->
            fileService.persistFile(file)
        }
        return true
    }
}