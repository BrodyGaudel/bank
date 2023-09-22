package com.mounanga.accountservice.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c ->
                        c.requestMatchers(HttpMethod.DELETE, "/accounts/delete/**").hasAuthority(ADMIN)
                                .requestMatchers(HttpMethod.POST, "/accounts/create").hasAnyAuthority(ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/accounts/update-status/**").hasAnyAuthority(ADMIN)
                                .requestMatchers(HttpMethod.GET, "/accounts/find/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/accounts/get/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/operations/get/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/operations/{accountId}/history/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.POST, "/operations/credit/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.POST, "/operations/debit/**").hasAnyAuthority(USER, ADMIN)
                                .anyRequest().authenticated());
        http.addFilterBefore(new JWTAuthorizationFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }
}
