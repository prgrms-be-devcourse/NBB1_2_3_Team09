package medinine.pill_buddy.domain.medicationApi.repository

import medinine.pill_buddy.domain.medicationApi.entity.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MedicationApiRepository : JpaRepository<Medication, String> {
    @Query("""select m from Medication m where m.itemName like %:itemName%""")
    fun findPageByItemNameLike(itemName: String, pageable: Pageable): Page<Medication>

    @Query("select m from Medication m where m.itemName like %:itemName%")
    fun findAllByItemName(itemName: String) : List<Medication>

    @Query("select distinct m.keyword from Medication m ")
    fun findAllByDistinctItemName() : List<String>

    fun findByItemSeq(long: Long) : Medication
}
