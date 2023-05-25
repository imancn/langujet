package com.cn.speaktest.application.file.http

import com.cn.speaktest.application.file.data.model.File
import com.cn.speaktest.application.file.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/file/")
class FileController(
    private val fileService: FileService,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam file: MultipartFile,
        @RequestParam format: String,
        @RequestParam dir: String?,
        @RequestParam bucket: String?,
    ): ResponseEntity<String> {
        return try {
            fileService.storeFileToKafka(file, format, dir, bucket)
            ResponseEntity.ok("File uploaded successfully.\nyou can fetch it with a few delay.")
        } catch (e: Exception) {
            logger.error("Upload api is not available.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.")
        }
    }

    @PostMapping("/persist")
    fun persistFile(
        @RequestParam file: MultipartFile,
        @RequestParam format: String,
        @RequestParam dir: String?,
        @RequestParam bucket: String?,
    ): ResponseEntity<String> {
        return try {
            fileService.persistFile(file, format, dir, bucket)
            ResponseEntity.ok("File uploaded successfully.")
        } catch (e: Exception) {
            logger.error("Upload api is not available.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.")
        }
    }

    @GetMapping("/download/{id}")
    fun downloadFile(
        @PathVariable id: String,
    ): ResponseEntity<File> {
        val file = fileService.downloadFile(id)
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"${file?.name}\"").body(file)
    }

    @PostMapping("/file/delete/{id}")
    fun deleteFile(
        @PathVariable id: String
    ): ResponseEntity<String> {
        fileService.deleteFile(id)
        return ResponseEntity("File Deleted", HttpStatus.OK)
    }
}