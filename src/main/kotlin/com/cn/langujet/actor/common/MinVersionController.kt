package com.cn.langujet.actor.common

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/student/min-version")
class MinVersionController {
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    fun getDeviceAcceptableMinVersion(): DeviceAcceptableMinVersion {
        return DeviceAcceptableMinVersion("1.0.0")
    }
}

data class DeviceAcceptableMinVersion(
    val minVersion: String
)