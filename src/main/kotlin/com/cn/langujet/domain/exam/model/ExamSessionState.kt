package com.cn.langujet.domain.exam.model

enum class ExamSessionState(val order: Int) {
    ENROLLED(1),
    STARTED(2),
    FINISHED(3),
    EVALUATED(4)
}
