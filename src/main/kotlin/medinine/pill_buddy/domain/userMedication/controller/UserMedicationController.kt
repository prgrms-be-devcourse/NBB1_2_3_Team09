package medinine.pill_buddy.domain.userMedication.controller

import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers/{caretakerId}/user-medications")
class UserMedicationController (
    private val userMedicationService: UserMedicationService
){
    @PostMapping
    fun createUserMedication(
        @PathVariable caretakerId: Long,
        @RequestBody userMedicationDTO: UserMedicationDTO
    ): ResponseEntity<UserMedicationDTO> {
        val savedUserMedication = userMedicationService.register(caretakerId, userMedicationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserMedication)
    }
}