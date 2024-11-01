package medinine.pill_buddy.domain.user.caretaker.dto

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver

data class CaretakerCaregiverDTO (
    val id: Long? = null,
    val caretaker: Caretaker? = null,
    val caregiver: Caregiver? = null
) {
    companion object {
        fun entityToDTO(caretakerCaregiver: CaretakerCaregiver): CaretakerCaregiverDTO {
            return CaretakerCaregiverDTO(
                id = caretakerCaregiver.id,
                caretaker = caretakerCaregiver.caretaker,
                caregiver = caretakerCaregiver.caregiver
            )
        }
    }
}