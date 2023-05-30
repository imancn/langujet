package com.cn.speaktest.application.file.data.repository

import com.cn.speaktest.application.file.data.model.Directory
import com.datastax.oss.driver.api.core.CqlSession
import org.springframework.stereotype.Repository

@Repository
class DirectoryRepositoryImpl(private val session: CqlSession) : DirectoryRepository {

    override fun createDirectory(directory: Directory): Directory {
        val query = "INSERT INTO directory (id, name, files, directories, path_from_root) VALUES (?, ?, ?, ?, ?)"
        val statement = session.prepare(query).bind(
            directory.id, directory.name, directory.files, directory.directories, directory.pathFromRoot
        )
        session.execute(statement)
        return directory
    }

    override fun getDirectoryById(id: String): Directory? {
        val query = "SELECT * FROM directory WHERE id = ?"
        val statement = session.prepare(query).bind(id)
        val result = session.execute(statement)
        val row = result.one()
        return if (row != null) Directory(
            id = row.getString("id"),
            name = row.getString("name"),
            files = row.getList("files", String::class.java),
            directories = row.getList("directories", String::class.java),
            pathFromRoot = row.getString("path_from_root")
        ) else null
    }

    override fun updateDirectory(directory: Directory): Directory {
        val query = "UPDATE directory SET name = ?, files = ?, directories = ?, path_from_root = ? WHERE id = ?"
        val statement = session.prepare(query).bind(
            directory.name, directory.files, directory.directories, directory.pathFromRoot, directory.id
        )
        session.execute(statement)
        return directory
    }

    override fun deleteDirectoryById(id: String) {
        val query = "DELETE FROM directory WHERE id = ?"
        val statement = session.prepare(query).bind(id)
        session.execute(statement)
    }
}