package com.cn.langujet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories
@EnableFeignClients
@EnableMongoAuditing
@EnableScheduling
class LangujetApplication

fun main(args: Array<String>) {
    runApplication<LangujetApplication>(*args)
}
