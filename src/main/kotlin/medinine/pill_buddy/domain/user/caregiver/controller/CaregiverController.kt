package medinine.pill_buddy.domain.user.caregiver.controller

import medinine.pill_buddy.domain.user.caregiver.service.CaregiverService
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/caregivers/{caregiverId}/caretakers")
@Tag(name = "보호자 기능", description = "보호자는 사용자를 등록, 삭제할 수 있으며, 사용자의 약 정보를 확인할 수 있다.")
class CaregiverController(
    private val caregiverService: CaregiverService
) {

    @Operation(description = "보호자는 사용자의 약 정보를 확인할 수 있다.")
    @GetMapping("/{caretakerId}/caretaker-medications")
    fun getCaretakerMedications(
        @PathVariable caregiverId: Long,
        @PathVariable caretakerId: Long
    ): ResponseEntity<List<UserMedicationDTO>> {
        val medications = caregiverService.getCaretakerMedications(caregiverId, caretakerId)
        return ResponseEntity.status(HttpStatus.OK).body(medications)
    }

    @Operation(description = "보호자는 사용자를 등록할 수 있다.")
    @PostMapping
    fun addCaretaker(
        @PathVariable caregiverId: Long,
        @RequestParam caretakerId: Long
    ): ResponseEntity<CaretakerCaregiverDTO> {
        val savedCaretakerCaregiverDTO = caregiverService.register(caregiverId, caretakerId)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO)
    }

    @Operation(description = "보호자는 사용자를 삭제할 수 있다.")
    @DeleteMapping("/{caretakerId}")
    fun deleteCaretaker(
        @PathVariable caregiverId: Long,
        @PathVariable caretakerId: Long
    ): ResponseEntity<Map<String, String>> {
        caregiverService.remove(caregiverId, caretakerId)
        val result = mapOf("Process" to "Success")
        return ResponseEntity.ok(result)
    }
}