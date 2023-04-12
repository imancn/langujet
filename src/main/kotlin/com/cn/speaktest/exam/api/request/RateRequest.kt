package com.cn.speaktest.exam.api.request

import com.cn.speaktest.exam.model.Suggestion

class RateRequest(
    val score: Double,
    val suggestion: Suggestion
)
