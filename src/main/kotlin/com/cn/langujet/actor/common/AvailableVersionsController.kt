package com.cn.langujet.actor.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/student/mobile-app/available-versions")
class AvailableVersionsController {
    @GetMapping
    fun getMobileAppAvailableVersions(): MobileAppAvailableVersions {
        return MobileAppAvailableVersions(
            minimum = "1.0.0",
            latest = "1.0.0"
        )
    }
}

data class MobileAppAvailableVersions(
    val minimum: String,
    val latest: String
)