package com.cn.speaktest.application.file.domain.service

import com.cn.speaktest.application.file.domain.data.mongo.model.File
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun persistFile(file: MultipartFile, format: String, dirId: String?, bucket: String?): File
    fun storeFileToKafka(multipartFile: MultipartFile, format: String, dirId: String?, bucket: String?): File
    fun downloadFile(id: String): File?
    fun persistFile(file: File): File
    fun deleteFile(id: String)
}
