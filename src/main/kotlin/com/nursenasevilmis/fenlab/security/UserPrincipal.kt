package com.nursenasevilmis.fenlab.security

import com.nursenasevilmis.fenlab.model.User
import com.nursenasevilmis.fenlab.model.enums.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

data class UserPrincipal(
    val id: Long,
    private val username: String,
    private val password: String,
    val email: String,
    val role: UserRole,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    companion object {
        fun create(user: User): UserPrincipal {
            val authorities = listOf(SimpleGrantedAuthority(user.role.name))

            return UserPrincipal(
                id = user.id!!,
                username = user.username,
                password = user.password,
                email = user.email,
                role = user.role,
                authorities = authorities
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
