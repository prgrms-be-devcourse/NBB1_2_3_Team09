package medinine.pill_buddy.domain.medicationApi.dto

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.constraints.NotBlank


data class MedicationForm(
    @Parameter(description = "약이름(필수값)")
    @field:NotBlank
    var itemName: String? =  null,

    @Parameter(description = "회사명")
    val entpName: String? = null,

    @Parameter(description = "품목기준코드")
    val itemSeq: String? = null
)