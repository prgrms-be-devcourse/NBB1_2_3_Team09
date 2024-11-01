package medinine.pill_buddy.domain.user.caretaker.service

import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO

interface CaretakerService {
    fun register(caretakerId: Long, caregiverId: Long): CaretakerCaregiverDTO
    fun remove(caretakerId: Long, caregiverId: Long)
}