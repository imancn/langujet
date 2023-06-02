package com.cn.speaktest.application.file.actor.http.controller

import com.cn.speaktest.application.file.domain.data.mongo.model.Directory
import com.cn.speaktest.application.file.domain.service.DirectoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/dir/")
class DirectoryController(private val directoryService: DirectoryService) {

    @PostMapping
    fun createDirectory(@RequestBody directory: Directory): Directory {
        return directoryService.createDirectory(directory)
    }

    @GetMapping("/{id}")
    fun getDirectoryById(@PathVariable id: String): Directory? {
        return directoryService.getDirectoryById(id)
    }

    @PostMapping("/{id}")
    fun updateDirectory(
        @PathVariable id: String,
        @RequestBody directory: Directory
    ): Directory {
        return directoryService.updateDirectory(id, directory)
    }

    @PostMapping("/{id}")
    fun deleteDirectoryById(@PathVariable id: String) {
        directoryService.deleteDirectoryById(id)
    }
}