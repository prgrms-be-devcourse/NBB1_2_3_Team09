package medinine.pill_buddy.domain.medicationApi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MedicationDTO(
    var itemName: String? = null,

    var entpName: String? = null,

    var itemSeq: Long? = null,

    var efcyQesitm: String? = null,

    var useMethodQesitm: String? = null,

    var atpnWarnQesitm: String? = null,

    var atpnQesitm: String? = null,

    var intrcQesitm: String? = null,

    var seQesitm: String? = null,

    var depositMethodQesitm: String? = null,

    @JsonProperty("itemImage")
    var itemImagePath: String? = null
)
