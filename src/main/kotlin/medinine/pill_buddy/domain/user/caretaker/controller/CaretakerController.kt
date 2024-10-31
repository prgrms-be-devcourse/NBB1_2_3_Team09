package medinine.pill_buddy.domain.user.caretaker.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.service.CaretakerService
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers")
class CaretakerController(
    private val caretakerService: CaretakerService,
    private val userMedicationService: UserMedicationService
) {
    @PostMapping("/{caretakerId}/caregivers/{caregiverId}")
    fun addCaregiver(
        @PathVariable caretakerId: Long,
        @PathVariable caregiverId: Long
    ): ResponseEntity<CaretakerCaregiverDTO> {
        val savedCaretakerCaregiverDTO = caretakerService.register(caretakerId, caregiverId)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO)
    }
}