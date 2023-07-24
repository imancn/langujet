package com.cn.langujet.application.service.file.actor.http.controller

import com.cn.langujet.application.service.file.domain.data.mongo.model.Directory
import com.cn.langujet.application.service.file.domain.service.DirectoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/dir/")
class DirectoryController(private val directoryService: DirectoryService) {

    @PostMapping
    fun createDirectory(
        @RequestBody directory: Directory
    ): Directory {
        return directoryService.createDirectory(directory)
    }

    @GetMapping("/")
    fun getDirectoryById(
        @RequestParam id: String,
    ): Directory? {
        return directoryService.getDirectoryById(id)
    }

    @PostMapping("/update")
    fun updateDirectory(
        @RequestBody directory: Directory
    ): Directory {
        return directoryService.updateDirectory(directory)
    }

    @PostMapping("delete/")
    fun deleteDirectoryById(
        @RequestParam id: String,
    ) {
        directoryService.deleteDirectoryById(id)
    }
}