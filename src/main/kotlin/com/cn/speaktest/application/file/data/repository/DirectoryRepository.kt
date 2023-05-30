package com.cn.speaktest.application.file.data.repository

import com.cn.speaktest.application.file.data.model.Directory

interface DirectoryRepository {
    fun createDirectory(directory: Directory): Directory
    fun getDirectoryById(id: String): Directory?
    fun updateDirectory(directory: Directory): Directory
    fun deleteDirectoryById(id: String)
}