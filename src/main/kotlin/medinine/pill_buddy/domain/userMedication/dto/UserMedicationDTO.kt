package medinine.pill_buddy.domain.userMedication.dto

import medinine.pill_buddy.domain.userMedication.entity.Frequency
import medinine.pill_buddy.domain.userMedication.entity.MedicationType
import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import java.time.LocalDateTime

data class UserMedicationDTO(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var dosage: Int? = null,
    var frequency: Frequency? = null,
    var type: MedicationType? = null,
    var stock: Int = 0,
    var expirationDate: LocalDateTime? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null
) {
    fun toEntity(): UserMedication {
        return UserMedication(
            name = this.name ?: "",
            description = this.description,
            dosage = this.dosage,
            frequency = this.frequency ?: Frequency.ONCE_A_DAY,
            type = this.type ?: MedicationType.MEDICATION,
            stock = this.stock,
            expirationDate = this.expirationDate ?: LocalDateTime.now().plusMonths(1),
            startDate = this.startDate ?: LocalDateTime.now(),
            endDate = this.endDate ?: LocalDateTime.now().plusMonths(1)
        )
    }
    companion object {
        fun entityToDTO(userMedication: UserMedication): UserMedicationDTO {
            return UserMedicationDTO(
                id = userMedication.id,
                name = userMedication.name,
                description = userMedication.description,
                dosage = userMedication.dosage,
                frequency = userMedication.frequency,
                type = userMedication.type,
                stock = userMedication.stock,
                expirationDate = userMedication.expirationDate,
                startDate = userMedication.startDate,
                endDate = userMedication.endDate
            )
        }
    }
}