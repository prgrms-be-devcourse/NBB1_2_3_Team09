package medinine.pill_buddy.domain.user.caretaker.repository

import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import org.springframework.data.jpa.repository.JpaRepository


interface CaretakerRepository : JpaRepository<Caretaker, Long> {

    fun findByEmail(email: String): Caretaker?
    fun findByLoginId(loginId: String): Caretaker?
    fun existsByLoginId(loginId: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}