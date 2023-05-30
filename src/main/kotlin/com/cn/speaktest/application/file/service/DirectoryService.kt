package com.cn.speaktest.application.file.service

import com.cn.speaktest.application.file.data.model.Directory
import com.cn.speaktest.application.file.data.repository.DirectoryRepository
import org.springframework.stereotype.Service

@Service
class DirectoryService(private val directoryRepository: DirectoryRepository) {

    fun createDirectory(directory: Directory): Directory {
        return directoryRepository.createDirectory(directory)
    }

    fun getDirectoryById(id: String): Directory? {
        return directoryRepository.getDirectoryById(id)
    }

    fun updateDirectory(directory: Directory): Directory {
        return directoryRepository.updateDirectory(directory)
    }

    fun deleteDirectoryById(id: String) {
        directoryRepository.deleteDirectoryById(id)
    }
}