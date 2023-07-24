package com.cn.langujet.application.service.file.domain.service

import com.cn.langujet.application.service.file.domain.data.mongo.model.File
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun persistFile(file: MultipartFile, format: String, dirId: String?, bucket: String?): File
    fun storeFileToKafka(multipartFile: MultipartFile, format: String, dirId: String?, bucket: String?): File
    fun getFileById(id: String): File
    fun persistFile(file: File): File
    fun deleteFile(id: String)
}