package com.cn.langujet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
@EnableFeignClients
class LangujetApplication

fun main(args: Array<String>) {
    runApplication<LangujetApplication>(*args)
}
