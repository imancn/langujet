package com.cn.speaktest.application.file.http.model

import com.cn.speaktest.application.file.data.model.File

data class FileMeta(
    val id: String?,
    val name: String,
    val contentType: String?,
    val format: String,
    val bucket: String?,
    val dirId: String?,
    val size: Long?,
) {
    constructor(file: File) : this(
        id = file.id,
        name = file.name,
        contentType = file.contentType,
        format = file.format,
        bucket = file.bucket,
        dirId = file.dirId,
        size = file.size,
    )
}