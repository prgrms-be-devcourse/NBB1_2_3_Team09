package medinine.pill_buddy.domain.userMedication.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import medinine.pill_buddy.domain.userMedication.dto.UserMedicationDTO
import medinine.pill_buddy.domain.userMedication.service.UserMedicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caretakers/{caretakerId}/user-medications")
@Tag(name = "사용자 약 정보 기능", description = "사용자는 자신이 복용하는 약을 등록,조회,수정,삭제할 수 있다.")
class UserMedicationController (
    private val userMedicationService: UserMedicationService
){

    @Operation(description = "사용자는 자신이 복용하는 약을 등록할 수 있다.")
    @PostMapping
    fun createUserMedication(
        @PathVariable caretakerId: Long, @RequestBody userMedicationDTO: UserMedicationDTO
    ): ResponseEntity<UserMedicationDTO> {
        val savedUserMedication = userMedicationService.register(caretakerId, userMedicationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserMedication)
    }

    @Operation(description = "사용자는 자신이 복용하는 약을 조회할 수 있다.")
    @GetMapping
    fun getUserMedications(@PathVariable caretakerId: Long): ResponseEntity<List<UserMedicationDTO>> {
        val userMedicationDTOList = userMedicationService.retrieve(caretakerId)

        return ResponseEntity.ok(userMedicationDTOList)
    }

    @Operation(description = "사용자는 자신이 복용하는 약 정보를 수정할 수 있다.")
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