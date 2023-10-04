package com.cn.langujet.application.service.file.domain.data.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class File(
    @Id
    val id: String,
    val name: String,
    val contentType: String?,
    val format: String,
    val bucket: String?,
    var value: ByteArray,
    val dirId: String?,
    val size: Long?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as File

        if (id != other.id) return false
        if (name != other.name) return false
        if (contentType != other.contentType) return false
        if (format != other.format) return false
        if (bucket != other.bucket) return false
        if (!value.contentEquals(other.value)) return false
        if (dirId != other.dirId) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + format.hashCode()
        result = 31 * result + (bucket?.hashCode() ?: 0)
        result = 31 * result + value.contentHashCode()
        result = 31 * result + (dirId?.hashCode() ?: 0)
        result = 31 * result + (size?.hashCode() ?: 0)
        return result
    }
}