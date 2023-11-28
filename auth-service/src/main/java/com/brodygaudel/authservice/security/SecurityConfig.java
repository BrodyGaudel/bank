package com.brodygaudel.authservice.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";

    private final JWTAuthorizationFilter authorizationFilter;

    public SecurityConfig(JWTAuthorizationFilter authorizationFilter) {
        this.authorizationFilter = authorizationFilter;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/list").hasAuthority(ADMIN)
                        .requestMatchers("/save/user/**").hasAuthority(ADMIN)
                        .requestMatchers("/get/user/**").hasAuthority(ADMIN)
                        .requestMatchers("/save/role/**").hasAuthority(ADMIN)
                        .requestMatchers("/add-role-to-user/**").hasAuthority(ADMIN)
                        .requestMatchers("/remove-role-to-user/**").hasAuthority(ADMIN)
                        .requestMatchers("/delete/**").hasAuthority(ADMIN)
                        .requestMatchers("/login").permitAll())
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
