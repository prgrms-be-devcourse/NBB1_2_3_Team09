package medinine.pill_buddy.domain.user.profile.entity

import jakarta.persistence.*
import medinine.pill_buddy.global.entity.BaseTimeEntity

@Entity
@Table(name = "image")
class Image(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    var id : Long? = null,

    @Column(name = "url", nullable = false)
    var url : String

) : BaseTimeEntity() {
    fun updateUrl(url : String) {
        this.url = url
    }
}