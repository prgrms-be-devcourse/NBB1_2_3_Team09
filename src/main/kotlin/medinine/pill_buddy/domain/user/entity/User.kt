package medinine.pill_buddy.domain.user.entity

import jakarta.persistence.*
import medinine.pill_buddy.domain.user.profile.entity.Image
import medinine.pill_buddy.global.entity.BaseTimeEntity

@MappedSuperclass
abstract class User(

    @Column(name = "username", length = 30, nullable = false)
    var username: String,

    @Column(name = "login_id", length = 20, unique = true, nullable = false)
    var loginId: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "email", length = 50, unique = true, nullable = false)
    var email: String,

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    var phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role,

    @OneToOne(cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    var image: Image? = null
) : BaseTimeEntity() {

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updateLoginId(loginId: String) {
        this.loginId = loginId
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateImage(image: Image?) {
        this.image = image
    }
}