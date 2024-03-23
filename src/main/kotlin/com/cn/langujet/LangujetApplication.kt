package com.cn.langujet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
class LangujetApplication

fun main(args: Array<String>) {
    runApplication<LangujetApplication>(*args)
}
