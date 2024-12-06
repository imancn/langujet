package com.cn.langujet.domain.exam.model

import com.cn.langujet.application.shared.LogEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "exam_section_contents")
@TypeAlias("exam_section_contents")
class ExamSectionContentEntity(
    @Id
    var id: String?,
    var examId: String,
    var sectionOrder: Int,
    var fileId: String,
): LogEntity()
