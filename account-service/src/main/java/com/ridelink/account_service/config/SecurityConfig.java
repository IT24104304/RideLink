package com.ridelink.account_service.config;

import com.ridelink.account_service.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/accounts/register",
                        "/api/accounts/login",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/accounts/{id}").hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}/profile").hasAnyRole("PASSENGER", "DRIVER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/accounts").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}/role").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/accounts/{id}").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
