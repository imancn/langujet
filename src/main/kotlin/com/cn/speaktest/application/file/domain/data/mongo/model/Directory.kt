package com.cn.speaktest.application.file.domain.data.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Directory (
    @Id
    val id: String,
    // todo: name could not contains "/" or "\" or "."
    var name: String?,
    var files: List<String>?,
    var directories: List<String>?,
    var pathFromRoot: String?
)