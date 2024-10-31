package medinine.pill_buddy.domain.medicationApi.repository

import medinine.pill_buddy.domain.medicationApi.entity.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MedicationApiRepository : JpaRepository<Medication, String> {
    @Query("""select m from Medication m where m.itemName like %:itemName%""")
    fun findAllByItemNameLike(itemName: String, pageable: Pageable): Page<Medication>

    @Query("select distinct m.itemName from Medication m ")
    fun findAllByDistinctItemName() : List<String>
}
