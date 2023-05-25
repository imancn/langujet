package com.cn.speaktest.application.file.data.repository

import com.cn.speaktest.application.file.data.model.File

interface FileRepository {
    fun save(file: File)
    fun fetchFile(id: String): File?
    fun deleteById(id: String)
}