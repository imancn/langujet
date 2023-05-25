package com.cn.speaktest.application.file.config

import com.cn.speaktest.application.file.data.repository.FileRepository
import com.cn.speaktest.application.file.data.repository.FileRepositoryImpl
import com.datastax.oss.driver.api.core.CqlSession
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress

@Configuration
class CassandraConfig {
    @Value("\${cassandra.contact-points}")
    private lateinit var contactPoints: String

    @Value("\${cassandra.keyspace}")
    private lateinit var keyspace: String

    private lateinit var cassandraSession: CqlSession

    @PostConstruct
    fun init() {
        val cqlSessionBuilder = CqlSession.builder().withKeyspace(keyspace)

        val portHostPairs = contactPoints.split(",").map { cp ->
            cp.split(":").let {
                it[0].toInt() to it[1]
            }
        }

        portHostPairs.forEach {
            cqlSessionBuilder.addContactPoint(InetSocketAddress.createUnresolved(it.second, it.first))
        }

        cassandraSession = cqlSessionBuilder.build()

        // Make sure to close the session when the application shuts down
        Runtime.getRuntime().addShutdownHook( Thread { cassandraSession.close() } )
    }
    @Bean
    fun cassandraSession(): CqlSession = cassandraSession

    @Bean
    fun fileRepository(session: CqlSession): FileRepository = FileRepositoryImpl(session)
}