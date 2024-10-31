package medinine.pill_buddy.domain.medicationApi.dto

data class JsonForm(
    var totalCount : Int = 0,
    var nowPageNum : Int = 0,
    var maxPageNum : Int = 0,
    var data: List<MedicationDTO>? = null)
