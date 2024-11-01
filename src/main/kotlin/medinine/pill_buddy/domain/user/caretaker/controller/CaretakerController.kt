package medinine.pill_buddy.domain.user.caretaker.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.service.CaretakerService
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

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

    @DeleteMapping("/{caretakerId}/caregivers/{caregiverId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteCaregiver(
        @PathVariable caretakerId: Long, @PathVariable caregiverId: Long
    ): ResponseEntity<Map<String, String>> {
        caretakerService.remove(caretakerId, caregiverId)
        return ResponseEntity.ok(mapOf("Process" to "Success"))
    }

    @GetMapping("/{caretakerId}/user-medications/records")
    fun getRecordsByDate(
        @PathVariable caretakerId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<List<RecordDTO>> {
        val records = userMedicationService.getUserMedicationRecordsByDate(caretakerId, date.atStartOfDay())
        return ResponseEntity.ok(records)
    }
}