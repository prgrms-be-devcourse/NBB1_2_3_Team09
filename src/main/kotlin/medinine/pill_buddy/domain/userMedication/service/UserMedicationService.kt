package medinine.pill_buddy.domain.userMedication.service

import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO

interface UserMedicationService {
    fun register(caretakerId: Long, userMedicationDTO: UserMedicationDTO): UserMedicationDTO
}