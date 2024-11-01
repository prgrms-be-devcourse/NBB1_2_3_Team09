package medinine.pill_buddy.domain.user.caretaker.repository

import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.entity.CaretakerCaregiver
import org.springframework.data.jpa.repository.JpaRepository

interface CaretakerCaregiverRepository: JpaRepository<CaretakerCaregiver,Long>{
    fun findByCaretaker(caretaker: Caretaker): List<CaretakerCaregiver>
    fun findByCaretakerIdAndCaregiverId(caretakerId: Long, caregiverId: Long): CaretakerCaregiver?
}