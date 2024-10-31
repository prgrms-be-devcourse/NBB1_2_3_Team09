package medinine.pill_buddy.domain.user.service

import medinine.pill_buddy.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { "ROLE_${user.role}" })
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.loginId

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}