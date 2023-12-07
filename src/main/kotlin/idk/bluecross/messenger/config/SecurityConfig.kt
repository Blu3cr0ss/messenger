package idk.bluecross.messenger.config

import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.util.security.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    var userService: UserService,
    var jwtTokenFilter: JwtTokenFilter
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authCfg: AuthenticationConfiguration) = authCfg.authenticationManager

    @Primary
    @Override
    @Bean
    fun authManagerBuilder(authManagerBuilder: AuthenticationManagerBuilder) = authManagerBuilder.also {
        it
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests {
            it
                .anyRequest().permitAll()
        }
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
}