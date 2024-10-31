package medinine.pill_buddy.domain.user.caregiver.repository

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import org.springframework.data.jpa.repository.JpaRepository

interface CaregiverRepository : JpaRepository<Caregiver, Long> {

    fun findByLoginId(loginId: String): Caregiver?
    fun existsByLoginId(loginId: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}