package com.cn.speaktest.application.file.http.controller

import com.cn.speaktest.application.file.data.model.Directory
import com.cn.speaktest.application.file.service.DirectoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/directories")
class DirectoryController(private val directoryService: DirectoryService) {

    @PostMapping
    fun createDirectory(@RequestBody directory: Directory): Directory {
        return directoryService.createDirectory(directory)
    }

    @GetMapping("/{id}")
    fun getDirectoryById(@PathVariable id: String): Directory? {
        return directoryService.getDirectoryById(id)
    }

    @PostMapping
    fun updateDirectory(@RequestBody directory: Directory): Directory {
        return directoryService.updateDirectory(directory)
    }

    @PostMapping("/{id}")
    fun deleteDirectoryById(@PathVariable id: String) {
        directoryService.deleteDirectoryById(id)
    }
}