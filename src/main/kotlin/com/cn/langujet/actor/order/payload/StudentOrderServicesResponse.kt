package com.cn.langujet.actor.order.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType

sealed class StudentOrderServicesResponse(
    var name: String,
    var type: ServiceType,
    var price: Double,
    var discount: Double,
) {
    companion object {
        inline fun <reified T : StudentOrderServicesResponse> from(service: ServiceEntity): T {
            val serviceResponse = when (service) {
                is ServiceEntity.ExamServiceEntity -> StudentOrderExamServicesResponse(service)
                else -> throw IllegalArgumentException("Unknown Service type")
            }
            if (serviceResponse !is T) throw TypeCastException("The type of question does not match the reified type.")
            return serviceResponse
        }
    }
}

class StudentOrderExamServicesResponse(
    name: String,
    price: Double,
    discount: Double,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
) : StudentOrderServicesResponse(
    name,
    ServiceType.EXAM,
    price,
    discount,
) {
    constructor(entity : ServiceEntity.ExamServiceEntity) : this(
        name = entity.name,
        price = entity.price,
        discount = entity.discount,
        examType = entity.examType,
        examMode = entity.examMode,
        correctorType = entity.correctorType,
    )
}