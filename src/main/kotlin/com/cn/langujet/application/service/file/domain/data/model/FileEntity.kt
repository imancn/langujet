package com.cn.langujet.application.service.file.domain.data.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
@TypeAlias("files")
class FileEntity(
    id: Long?,
    var name: String,
    val extension: String,
    var bucket: FileBucket,
    var contentType: String,
    var size: Long,
) : HistoricalEntity(id = id) {
    fun getObjectName() = "${id}.${extension}"
    fun getBucketName() = bucket.bucketName
}