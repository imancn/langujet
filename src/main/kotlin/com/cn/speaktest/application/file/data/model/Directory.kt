package com.cn.speaktest.application.file.data.model

class Directory (
    val id: String,
    val name: String,
    val files: List<String>,
    val directories: List<String>,
    val parent: Directory?
)