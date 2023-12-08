package com.cn.langujet.application.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "Langujet",
        description = "API documentation",
        contact = Contact(
            name = "Langujet",
            url = "https://langujet.com",
            email = "info@langujet.com"
        ),
        license = License(
            name = "All rights reserved for Langujet Co.",
            url = "https://langujet.com")
    ),
    servers = [Server(url = "/")]
)
class OpenAPIConfiguration