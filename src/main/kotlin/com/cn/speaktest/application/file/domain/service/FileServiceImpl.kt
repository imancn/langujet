package com.cn.speaktest.application.file.domain.service

import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.application.file.domain.data.mongo.model.File
import com.cn.speaktest.application.file.domain.data.mongo.repository.FileRepository
import com.cn.speaktest.application.kafka.producer.file.FileProducer
import org.springframework.stereotype.Component
import org.springframework.util.DigestUtils
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val fileProducer: FileProducer,
) : FileService {

    override fun storeFileToKafka(multipartFile: MultipartFile, format: String, dirId: String?, bucket: String?): File {
        return makeFile(format, bucket, dirId, multipartFile).also { fileProducer.sendFile(it) }
    }

    override fun persistFile(file: MultipartFile, format: String, dirId: String?, bucket: String?): File {
        return makeFile(format, bucket, dirId, file).also { persistFile(it) }
    }

    override fun persistFile(file: File): File {
        return fileRepository.save(file).also { it.value = ByteArray(1) }
    }

    override fun downloadFile(id: String): File? {
        return fileRepository.findById(id).orElseThrow {
            NotFoundException("Directory not found with id: $id")
        }
    }

    override fun deleteFile(id: String) {
        fileRepository.deleteById(id)
    }

    private fun makeFile(
        format: String, bucket: String?, dirId: String?, multipartFile: MultipartFile
    ): File {
        return File(
            id = "${multipartFile.name}-$format-$bucket-$dirId-${System.currentTimeMillis()}".toHash(),
            name = multipartFile.originalFilename ?: "Unknown",
            format = format,
            contentType = multipartFile.contentType ?: "application/octet-stream",
            bucket = bucket,
            value = multipartFile.bytes,
            dirId = dirId,
            size = multipartFile.size
        )
    }

    fun String.toHash(): String = UUID.nameUUIDFromBytes(DigestUtils.md5Digest(this.toByteArray())).toString()
}