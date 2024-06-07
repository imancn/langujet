package com.cn.langujet.domain.region

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClientRegionService(
    private val ipInfoClient: IpApiClient
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    
    fun isFromIran(ip: String): Boolean {
        try {
            return ipInfoClient.getIpInfo(ip).countryCode == "IR"
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            return false
        }
    }
}
