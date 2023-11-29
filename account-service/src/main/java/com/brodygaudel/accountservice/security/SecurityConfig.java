package com.brodygaudel.accountservice.security;

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
                        .requestMatchers("/v2/accounts/get/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/accounts/find/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/accounts/save/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/accounts/update/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/accounts/delete/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/operations/credit/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/operations/debit/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/operations/get/**").hasAuthority(ADMIN)
                        .requestMatchers("/v2/operations/{accountId}/all/**").hasAuthority(ADMIN)
                ).addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
