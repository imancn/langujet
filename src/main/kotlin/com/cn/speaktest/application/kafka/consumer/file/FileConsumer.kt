package com.cn.speaktest.application.kafka.consumer.file

import com.cn.speaktest.application.file.data.model.File
import com.cn.speaktest.application.file.service.FileService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class FileConsumer(private val fileService: FileService) {
    @KafkaListener(topics = ["files"], groupId = "file-consumer")
    fun consumeFile(file: File) {
        fileService.persistFile(file)
    }
}