package no.nav.pensjon

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/actuator/health/liveness", "/actuator/health/readiness", "/actuator/prometheus").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                //Default
            }
            .logout{ logoutConfigurer ->
                logoutConfigurer.logoutSuccessUrl("/")
            }

        return http.build()
    }
}
