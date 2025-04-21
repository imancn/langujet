package com.cn.langujet.application.service.file.domain.service

import com.cn.langujet.application.arch.advice.InternalServerError
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.models.entity.Entity
import com.cn.langujet.application.service.file.domain.data.model.FileBucket
import com.cn.langujet.application.service.file.domain.data.model.FileEntity
import com.cn.langujet.application.service.file.domain.data.repository.FileRepository
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.http.Method
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val minioClient: MinioClient,
    private val fileRepository: FileRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun uploadFile(file: MultipartFile, bucket: FileBucket): FileEntity {
        val originalFileName = file.originalFilename ?: ""
        val extension = originalFileName.substringAfterLast('.', "")

        val fileEntity = fileRepository.save(
            FileEntity(
                id = null,
                name = originalFileName,
                extension = extension,
                bucket = bucket,
                contentType = file.contentType ?: "application/octet-stream",
                size = file.size
            )
        )

        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(fileEntity.getBucketName())
                    .`object`(fileEntity.getObjectName())
                    .stream(file.inputStream, file.size, -1)
                    .contentType(fileEntity.contentType)
                    .build()
            )
        } catch (ex: Exception) {
            fileRepository.deleteById(fileEntity.id ?: Entity.UNKNOWN_ID)
            logger.error(ex.toString())
            throw InternalServerError("Upload Failed")
        }

        return fileEntity
    }
    
    fun generatePublicDownloadLink(fileId: Long, expiryDuration: Int): String {
        val fileEntity = fileRepository.findById(fileId).orElseThrow {
            throw UnprocessableException("File with id: $fileId not found")
        }

        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(fileEntity.getBucketName())
                .`object`(fileEntity.getObjectName())
                .expiry(expiryDuration) // Duration in seconds
                .build()
        )
    }
}