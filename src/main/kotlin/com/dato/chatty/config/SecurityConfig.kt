package com.dato.chatty.config

import com.dato.chatty.model.Role
import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class SecurityConfig(
    private val userRepo: UserRepo
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .antMatchers("/private/**").hasAuthority(Role.ADMIN.name)
                    .anyRequest().authenticated()
            }
            .logout()
                .clearAuthentication(true)
                .deleteCookies()
                .invalidateHttpSession(true)
            .and().oauth2Login()
                .userInfoEndpoint()
                    .oidcUserService(oidcUserService())
        return http.build()
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest ->
            // Delegate to the default implementation for loading a user
            var oidcUser = delegate.loadUser(userRequest)
            val email = oidcUser.attributes["email"].toString()

            val user: User = userRepo.findByEmail(email) ?: userRepo.save(User(ObjectId.get(), email, setOf(Role.USER)))
            val mappedAuthorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }
            oidcUser = DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo)

            oidcUser
        }
    }

}