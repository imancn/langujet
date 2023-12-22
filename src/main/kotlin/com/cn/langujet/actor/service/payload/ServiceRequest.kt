package com.cn.langujet.actor.service.payload

import com.cn.langujet.domain.service.model.ServiceEntity
import com.cn.langujet.domain.service.model.ServiceType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

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
                name!!,
                price!!,
                discount!!,
                order!!,
                active!!,
                this.examVariantId!!
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
    @field:NotBlank override val name: String? = null,
    @field:NotNull override val price: Double? = null,
    @field:NotNull override val discount: Double? = null,
    @field:NotNull override val order: Int? = null,
    @field:NotNull override val active: Boolean? = null,
    @field:NotBlank val examVariantId: String? = null
): ServiceRequest(
    name,
    ServiceType.EXAM,
    price,
    discount,
    order,
    active
)