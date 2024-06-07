package com.cn.langujet.domain.region

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "ipapi", url = "https://freeipapi.com/")
interface IpApiClient {
    @GetMapping("/api/json/{ip}")
    fun getIpInfo(@PathVariable ip: String): IpApiResponse
}