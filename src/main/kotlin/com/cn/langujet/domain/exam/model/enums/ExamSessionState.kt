package com.cn.langujet.domain.exam.model.enums

enum class ExamSessionState(val order: Int) {
    ENROLLED(1),
    STARTED(2),
    FINISHED(3),
    CORRECTED(4)
}
