package com.dato.chatty.config

import com.dato.chatty.model.Role
import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import com.dato.chatty.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.dato.chatty.security.oauth2.OAuth2AuthenticationSuccessHandler
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
import org.springframework.security.web.firewall.FirewalledRequest
import org.springframework.security.web.firewall.StrictHttpFirewall
import javax.servlet.http.HttpServletRequest


@EnableWebSecurity
class SecurityConfig(
    private val userRepo: UserRepo,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler

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
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and().userInfoEndpoint()
                    .oidcUserService(oidcUserService())
                    .and().successHandler(oAuth2AuthenticationSuccessHandler)
        return http.build()
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest ->
            // Delegate to the default implementation for loading a user
            var oidcUser = delegate.loadUser(userRequest)
            val email = oidcUser.attributes["email"].toString()

            val userOpt = userRepo.findByEmail(email)
            val user: User = if (userOpt.isPresent) {
                userOpt.get()
            } else {
                userRepo.save(User(ObjectId.get(), email, setOf(Role.USER)))
            }

            val mappedAuthorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }
            oidcUser = DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo)

            oidcUser
        }
    }

    @Bean
    fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

}