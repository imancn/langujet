package com.cn.speaktest.application.file.data.repository

import com.cn.speaktest.application.file.data.model.File
import com.datastax.oss.driver.api.core.CqlSession
import org.springframework.stereotype.Repository

@Repository
class FileRepositoryImpl(
    private val session: CqlSession
) : FileRepository {

    private val saveStatement =
        session.prepare("INSERT INTO ${session.keyspace}.files (id, name, format, content_type, bucket, value, dir, size) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
    private val findStatement =
        session.prepare("SELECT name, format, content_type, bucket, value, dir, size FROM ${session.keyspace}.files WHERE id = ?")
    private val deleteStatement = session.prepare("DELETE FROM files WHERE id = ?")

    override fun save(file: File) {
        session.execute(
            saveStatement.bind(
                file.id,
                file.name,
                file.format,
                file.contentType,
                file.bucket,
                file.value,
                file.dirId,
                file.size
            )
        )
    }

    override fun fetchFile(id: String): File? {
        val resultSet = session.execute(findStatement.bind(id))
        val row = resultSet.one()

        return if (row != null) {
            File(
                id = id,
                name = row.getString("name") ?: "UNKNOWN",
                format = row.getString("format") ?: "UNKNOWN",
                contentType = row.getString("content_type"),
                bucket = row.getString("bucket"),
                value = row.getByteBuffer("value")?.array() ?: ByteArray(0),
                dirId = row.getString("dir"),
                size = row.getLong("size"),
            )
        } else null
    }

    override fun deleteById(id: String) {
        val boundStmt = deleteStatement.bind(id)
        session.execute(boundStmt)
    }
}