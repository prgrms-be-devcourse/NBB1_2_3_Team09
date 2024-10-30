package medinine.pill_buddy.domain.user.caretaker.repository

import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import org.springframework.data.jpa.repository.JpaRepository

interface CaretakerRepository: JpaRepository<Caretaker, Long>{
}