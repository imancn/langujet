package com.cn.speaktest.application.file.data.model

class Directory (
    val id: String?,
    // todo: name could not contains "/" or "\" or "."
    val name: String?,
    val files: List<String>?,
    val directories: List<String>?,
    var pathFromRoot: String?
)