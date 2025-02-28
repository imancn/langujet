package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.correction.model.CorrectorType
import com.cn.langujet.domain.exam.model.enums.ExamMode
import com.cn.langujet.domain.exam.model.enums.ExamType
import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(subTypes = [ExamServiceRequest::class])
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ExamServiceRequest::class, name = "EXAM"),
)
sealed class ServiceRequest(
    open val name: String?,
    open val type: ServiceType,
    open val price: Double?,
    open val discount: Double?,
    open val order: Int?,
    open val active: Boolean?
) {
    inline fun <reified T : ServiceEntity> convertToServiceEntity(): T {
        val service: ServiceEntity = when (this) {
            is ExamServiceRequest -> ServiceEntity.ExamServiceEntity(
                name,
                price,
                discount,
                order,
                active,
                this.examType,
                this.examMode,
                this.correctorType
            )

            else -> throw IllegalArgumentException("Unsupported service request type")
        }
        if (service !is T) {
            throw IllegalArgumentException("The service type does not match the expected return type.")
        }
        return service
    }
}

data class ExamServiceRequest(
    @field:NotBlank
    override val name: String,
    override val price: Double,
    override val discount: Double,
    override val order: Int,
    override val active: Boolean,
    var examType: ExamType,
    var examMode: ExamMode,
    var correctorType: CorrectorType,
): ServiceRequest(
    name,
    ServiceType.EXAM,
    price,
    discount,
    order,
    active
)