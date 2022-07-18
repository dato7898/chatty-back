package com.dato.chatty.security

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Role
import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService(
    private val userRepo: UserRepo
) : OidcUserService() {

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        var oidcUser = super.loadUser(userRequest)
        val email = oidcUser.attributes["email"].toString()
        val firstname = oidcUser.attributes["given_name"].toString()
        val lastname = oidcUser.attributes["family_name"].toString()
        val googleImgUrl = oidcUser.attributes["picture"].toString()

        val userOpt = userRepo.findByEmail(email)
        val user: User = if (userOpt.isPresent) {
            val oldUser = userOpt.get()
            oldUser.firstname = firstname
            oldUser.lastname = lastname
            oldUser.googleImgUrl = googleImgUrl
            userRepo.save(oldUser)
        } else {
            userRepo.save(User(
                email,
                firstname,
                lastname,
                googleImgUrl,
                setOf(Role.USER)
            ))
        }

        val mappedAuthorities = user.roles.map {
            SimpleGrantedAuthority(it.name)
        }
        oidcUser = DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo)

        return oidcUser
    }

    fun loadUserByName(name: String): UserDetails {
        val user = userRepo.findByEmail(name).orElseThrow {
            ResourceNotFoundException("User", "name", name)
        }
        return UserPrincipal.create(user)
    }

}