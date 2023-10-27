package com.cn.langujet.application.service.file.actor.http.controller

import com.cn.langujet.application.service.file.actor.http.model.FileMeta
import com.cn.langujet.application.service.file.domain.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping("/api/file/")
class FileController(
    private val fileService: FileService,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    @PostMapping("/persist")
    fun persistFile(
        @RequestParam file: MultipartFile,
        @RequestParam format: String,
        @RequestParam dir: String?,
        @RequestParam bucket: String?,
    ): ResponseEntity<FileMeta> {
        return try {
            logger.info("File uploaded successfully.")
            ResponseEntity.ok(FileMeta(fileService.persistFile(file, format, dir, bucket)))
        } catch (e: Exception) {
            logger.error("Upload api is not available.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @GetMapping("/download/")
    fun downloadFile(
        @RequestParam id: String,
    ): ResponseEntity<Resource> {
        val file = fileService.getFileById(id)
        val resource = try {
            ByteArrayResource(file.value)
        } catch (e: IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build<Resource>()
        }
        val headers = HttpHeaders().also {
            it.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${resource.filename}")
        }
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource)
    }

    @GetMapping("/")
    fun getFileMeta(
        @RequestParam id: String,
    ): ResponseEntity<FileMeta> {
        return ResponseEntity(FileMeta(fileService.getFileById(id)), HttpStatus.OK)
    }

    @PostMapping("/delete/")
    fun deleteFile(
        @RequestParam id: String,
    ): ResponseEntity<String> {
        fileService.deleteFile(id)
        return ResponseEntity("File Deleted", HttpStatus.OK)
    }
}