package com.cn.speaktest.application.file.service

import com.cn.speaktest.application.file.data.model.File
import com.cn.speaktest.application.file.data.repository.FileRepository
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

    override fun persistFile(file: File) {
        return fileRepository.save(file)
    }

    override fun downloadFile(id: String): File? {
        return fileRepository.fetchFile(id)
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