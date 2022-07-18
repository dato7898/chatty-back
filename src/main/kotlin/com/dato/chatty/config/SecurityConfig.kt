package com.dato.chatty.config

import com.dato.chatty.security.CustomOidcUserService
import com.dato.chatty.security.RestAuthenticationEntryPoint
import com.dato.chatty.security.TokenAuthenticationFilter
import com.dato.chatty.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.dato.chatty.security.oauth2.OAuth2AuthenticationFailureHandler
import com.dato.chatty.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customOidcUserService: CustomOidcUserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val tokenAuthenticationFilter: TokenAuthenticationFilter

) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors()
            .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf()
                .disable()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(RestAuthenticationEntryPoint())
            .and().authorizeHttpRequests { authz ->
                authz
                    .antMatchers("/ws/**").permitAll()
                    .antMatchers("/v2/api-docs/**").permitAll()
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
                    .oidcUserService(customOidcUserService)
                    .and().successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

}