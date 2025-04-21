package com.cn.langujet.domain.service.model

import com.cn.langujet.application.arch.models.entity.HistoricalEntity
import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "services")
sealed class ServiceEntity(
    id: Long?,
    var name: String,
    var type: ServiceType,
    var price: Double,
    var discount: Double,
    var order: Int,
    var active: Boolean
) : HistoricalEntity(id = id) {
    @Document(collation = "services")
    @TypeAlias("exam_services")
    class ExamServiceEntity(
        name: String,
        price: Double,
        discount: Double,
        order: Int,
        active: Boolean,
        var examType: ExamType,
        var examMode: ExamMode,
        var correctorType: CorrectorType,
    ) : ServiceEntity(
        null,
        name,
        ServiceType.EXAM,
        price,
        discount,
        order,
        active
    )
}