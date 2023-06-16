package com.cn.speaktest.application.thirdparty.file.domain.service

import com.cn.speaktest.application.advice.NotFoundException
import com.cn.speaktest.application.thirdparty.file.domain.data.mongo.model.Directory
import com.cn.speaktest.application.thirdparty.file.domain.data.mongo.repository.DirectoryRepository
import org.springframework.stereotype.Service

@Service
class DirectoryService(private val directoryRepository: DirectoryRepository) {

    fun createDirectory(directory: Directory): Directory {
        return directoryRepository.save(directory)
    }

    fun getDirectoryById(id: String): Directory {
        return directoryRepository.findById(id)
            .orElseThrow { NotFoundException("Directory not found with id: $id") }
    }

    fun updateDirectory(dir: Directory): Directory {
        val existingDirectory = getDirectoryById(dir.id)

        dir.name?.let { existingDirectory.name = it }
        dir.files?.let { existingDirectory.files = it }
        dir.directories?.let { existingDirectory.directories = it }
        dir.pathFromRoot?.let { existingDirectory.pathFromRoot = it }

        return directoryRepository.save(existingDirectory)
    }

    fun deleteDirectoryById(id: String) {
        directoryRepository.delete(getDirectoryById(id))
    }
}