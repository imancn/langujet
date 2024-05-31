package com.cn.langujet.domain.region

import org.springframework.stereotype.Service

@Service
class ClientRegionService(
    private val ipInfoClient: IpApiClient
) {
    fun isFromIran(ip: String): Boolean {
        return ipInfoClient.getIpInfo(ip).country == "IR"
    }
}
