package com.cn.speaktest.actor.exam.payload.request

import com.cn.speaktest.domain.exam.model.Suggestion

class RateRequest(
    val score: Double,
    val suggestion: Suggestion
)
