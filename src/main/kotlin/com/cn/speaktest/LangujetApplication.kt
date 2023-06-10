package com.cn.speaktest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class LangujetApplication

fun main(args: Array<String>) {
    runApplication<LangujetApplication>(*args)
}
