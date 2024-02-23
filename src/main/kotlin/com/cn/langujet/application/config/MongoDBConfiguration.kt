package com.cn.langujet.application.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories("com.cn.langujet")
class MongoDBConfiguration : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String
    override fun mongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String {
        return databaseName
    }
}