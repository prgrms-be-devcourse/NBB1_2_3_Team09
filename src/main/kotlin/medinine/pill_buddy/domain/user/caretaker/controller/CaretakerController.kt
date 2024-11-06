package medinine.pill_buddy.domain.user.caretaker.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.record.dto.RecordDTO
import medinine.pill_buddy.domain.record.service.RecordService
import medinine.pill_buddy.domain.user.caretaker.dto.CaretakerCaregiverDTO
import medinine.pill_buddy.domain.user.caretaker.service.CaretakerService
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 기능", description = "사용자는 보호자를 등록,삭제할 수 있으며, 복용 기록여부를 등록,조회할 수 있다.")
@RequestMapping("/api/caretakers")
class CaretakerController(
    private val caretakerService: CaretakerService,
    private val userMedicationService: UserMedicationService,
    private val recordService: RecordService
) {
    @Operation(description = "사용자는 보호자를 등록할 수 있다.")
    @PostMapping("/{caretakerId}/caregivers/{caregiverId}")
    fun addCaregiver(
        @PathVariable caretakerId: Long,
        @PathVariable caregiverId: Long
    ): ResponseEntity<CaretakerCaregiverDTO> {
        val savedCaretakerCaregiverDTO = caretakerService.register(caretakerId, caregiverId)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaretakerCaregiverDTO)
    }

    @Operation(description = "사용자는 보호자를 삭제할 수 있다.")
    @DeleteMapping("/{caretakerId}/caregivers/{caregiverId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteCaregiver(
        @PathVariable caretakerId: Long, @PathVariable caregiverId: Long
    ): ResponseEntity<Map<String, String>> {
        caretakerService.remove(caretakerId, caregiverId)
        return ResponseEntity.ok(mapOf("Process" to "Success"))
    }

    @Operation(description = "사용자는 지정일의 약 복용 기록을 조회할 수 있다.")
    @GetMapping("/{caretakerId}/user-medications/records")
    fun getRecordsByDate(
        @PathVariable caretakerId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<List<RecordDTO>> {
        val records = userMedicationService.getUserMedicationRecordsByDate(caretakerId, date.atStartOfDay())
        return ResponseEntity.ok(records)
    }

    @Operation(description = "사용자는 새로운 약의 복용 기록을 등록할 수 있다.")
    @PostMapping("/user-medications/{userMedicationId}/records")
    fun addRecord(@PathVariable userMedicationId: Long): ResponseEntity<RecordDTO> {
        val savedRecordDTO = recordService.registerRecord(userMedicationId)
        return ResponseEntity.ok(savedRecordDTO)
    }

    @Operation(description = "사용자는 약을 복용했다 표시할 수 있다.")
    @PutMapping("/user-medications/{userMedicationId}/records/{recordId}")
    fun updateRecord(
        @PathVariable userMedicationId: Long,
        @PathVariable recordId: Long
    ): ResponseEntity<RecordDTO> {
        val savedRecordDTO = recordService.modifyTaken(userMedicationId, recordId)
        return ResponseEntity.ok(savedRecordDTO)
    }
}