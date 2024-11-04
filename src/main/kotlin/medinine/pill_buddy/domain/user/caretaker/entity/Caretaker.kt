package medinine.pill_buddy.domain.user.caretaker.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.notification.entity.Notification
import medinine.pill_buddy.domain.user.entity.Role
import medinine.pill_buddy.domain.user.entity.User

@Entity
@Table(name = "caretaker")
class Caretaker(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caretaker_id")
    val id: Long? = null,

    @OneToMany(mappedBy = "caretaker", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val notification: MutableList<Notification> = ArrayList(),

    username: String,
    loginId: String,
    password: String,
    email: String,
    phoneNumber: String? = null
) : User(username, loginId, password, email, phoneNumber, Role.USER)