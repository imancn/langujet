package com.cn.speaktest.application.file.service

import com.cn.speaktest.application.file.data.model.File
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun persistFile(file: MultipartFile, format: String, dirId: String?, bucket: String?)
    fun storeFileToKafka(multipartFile: MultipartFile, format: String, dirId: String?, bucket: String?)
    fun downloadFile(id: String): File?
    fun persistFile(file: File)
    fun deleteFile(id: String)
}
