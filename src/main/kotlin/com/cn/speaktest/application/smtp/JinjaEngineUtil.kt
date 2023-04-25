package com.cn.speaktest.application.smtp

import com.hubspot.jinjava.Jinjava

object JinjaEngineUtil {

    private var engine = Jinjava()
    private val cachedTemplates = mutableMapOf<String, String>()

    fun render(name: String, map: Map<String, Any?>): String {
        return engine.render(templateOf(name), map) ?: throw RuntimeException("Cannot render $name")
    }

    private fun templateOf(name: String): String {
        return cachedTemplates[name] ?: run {
            val url = javaClass.getResource("/templates/$name") ?: throw RuntimeException("Template not found")
            url.readText().also { cachedTemplates[name] = it }
        }
    }
}