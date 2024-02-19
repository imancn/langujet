package com.cn.langujet.application.service.file.domain.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
data class FileEntity(
    @Id
    var id: String?,
    var name: String,
    val extension: String,
    var bucket: FileBucket,
    var contentType: String,
    var size: Long,
) {
    fun getObjectName() = "${id}.${extension}"
    fun getBucketName() = bucket.bucketName
}