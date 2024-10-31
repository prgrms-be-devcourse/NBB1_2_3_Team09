package medinine.pill_buddy.domain.userMedication.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers/{caretakerId}/user-medications")
class UserMedicationController (
    private val userMedicationService: UserMedicationService
){

    @PostMapping
    fun createUserMedication(
        @PathVariable caretakerId: Long, @RequestBody userMedicationDTO: UserMedicationDTO
    ): ResponseEntity<UserMedicationDTO> {
        val savedUserMedication = userMedicationService.register(caretakerId, userMedicationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserMedication)
    }

    @GetMapping
    fun getUserMedications(@PathVariable caretakerId: Long): ResponseEntity<List<UserMedicationDTO>> {
        val userMedicationDTOList = userMedicationService.retrieve(caretakerId)

        return ResponseEntity.ok(userMedicationDTOList)
    }

    @PutMapping("/{userMedicationId}")
    fun updateUserMedication(
        @PathVariable caretakerId: Long,
        @PathVariable userMedicationId: Long,
        @RequestBody userMedicationDTO: UserMedicationDTO
    ): ResponseEntity<UserMedicationDTO> {
        val updateUserMedication = userMedicationService.modify(caretakerId, userMedicationId, userMedicationDTO)
        return ResponseEntity.ok(updateUserMedication)
    }
}