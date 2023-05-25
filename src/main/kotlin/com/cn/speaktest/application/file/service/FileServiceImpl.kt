package com.cn.speaktest.application.file.service

import com.cn.speaktest.application.file.data.model.File
import com.cn.speaktest.application.file.data.repository.FileRepository
import com.cn.speaktest.application.kafka.producer.file.FileProducer
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val fileProducer: FileProducer,
) : FileService {

    override fun storeFileToKafka(multipartFile: MultipartFile, format: String, dirId: String?, bucket: String?) {
        val file = File(
            id = UUID.randomUUID().toString(),
            name = multipartFile.originalFilename ?: "Unknown",
            format = format,
            contentType = multipartFile.contentType ?: "application/octet-stream",
            bucket = bucket,
            value = multipartFile.bytes,
            dirId = dirId,
            size = multipartFile.size
        )
        fileProducer.sendFile(file)
    }

    override fun persistFile(file: MultipartFile, format: String, dirId: String?, bucket: String?) {
        persistFile(File(
            id = UUID.randomUUID().toString(),
            name = file.originalFilename ?: "Unknown",
            format = format,
            contentType = file.contentType ?: "application/octet-stream",
            bucket = bucket,
            value = file.bytes,
            dirId = dirId,
            size = file.size
        ))
    }

    override fun persistFile(file: File) {
        fileRepository.save(file)
    }

    override fun downloadFile(id: String): File? {
        return fileRepository.fetchFile(id)
    }

    override fun deleteFile(id: String) {
        fileRepository.deleteById(id)
    }
}