package medinine.pill_buddy.domain.userMedication.repository

import medinine.pill_buddy.domain.userMedication.entity.UserMedication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserMedicationRepository : JpaRepository<UserMedication, Long> {

    @Query("SELECT um FROM UserMedication um where um.caretaker.id = :caretakerId")
    fun findByCaretakerId(@Param("caretakerId") caretakerId: Long): List<UserMedication>
}