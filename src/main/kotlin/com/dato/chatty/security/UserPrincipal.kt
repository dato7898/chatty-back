package com.dato.chatty.security

import com.dato.chatty.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    private val email: String,
    private val authorities: Collection<GrantedAuthority>
) :
    OAuth2User, UserDetails {
    private var attributes: HashMap<String, Any>? = null

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getAttributes(): HashMap<String, Any>? {
        return attributes
    }

    fun setAttributes(attributes: HashMap<String, Any>?) {
        this.attributes = attributes
    }

    override fun getName(): String {
        return email
    }

    companion object {
        fun create(user: User): UserPrincipal {
            val mappedAuthorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }
            return UserPrincipal(
                user.email,
                mappedAuthorities
            )
        }
    }
}