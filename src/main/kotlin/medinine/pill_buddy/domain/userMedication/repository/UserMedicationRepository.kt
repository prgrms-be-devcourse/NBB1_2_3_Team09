package medinine.pill_buddy.domain.userMedication.repository

import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import org.springframework.data.jpa.repository.JpaRepository

interface UserMedicationRepository : JpaRepository<UserMedication, Long>
