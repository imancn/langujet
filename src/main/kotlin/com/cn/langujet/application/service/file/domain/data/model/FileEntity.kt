package com.cn.langujet.application.service.file.domain.data.model

import com.cn.langujet.application.shared.entity.LogEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
@TypeAlias("files")
data class FileEntity(
    @Id
    var id: String?,
    var name: String,
    val extension: String,
    var bucket: FileBucket,
    var contentType: String,
    var size: Long,
): LogEntity() {
    fun getObjectName() = "${id}.${extension}"
    fun getBucketName() = bucket.bucketName
}