package com.dato.chatty.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .anyRequest().authenticated()
            }
            .oauth2Login()
            .and().logout().clearAuthentication(true)
            .deleteCookies().invalidateHttpSession(true)
        return http.build()
    }

}