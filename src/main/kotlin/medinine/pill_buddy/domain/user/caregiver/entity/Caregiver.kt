package medinine.pill_buddy.domain.user.caregiver.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.user.entity.Role
import medinine.pill_buddy.domain.user.entity.User

@Entity
@Table(name = "caregiver")
class Caregiver(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiver_id")
    var id: Long? = null,

    username: String,
    loginId: String,
    password: String,
    email: String,
    phoneNumber: String
) : User(username, loginId, password, email, phoneNumber, Role.USER)