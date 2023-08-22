package com.mounanga.userservice.securities;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Autowired
    private AuthenticationManager authenticationManager;


    @Bean
    public AuthenticationManager authManager(@NotNull HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                             UserDetailsService userDetailsService)
            throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return managerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(
                        request -> {
                            CorsConfiguration config = new CorsConfiguration();

                            config.setAllowedOrigins(Collections.singletonList(SecParams.ORIGINS));
                            config.setAllowedMethods(Collections.singletonList(SecParams.ALL));
                            config.setAllowCredentials(true);
                            config.setAllowedHeaders(Collections.singletonList(SecParams.ALL));
                            config.setExposedHeaders(List.of(SecParams.AUTHORIZATION));
                            config.setMaxAge(SecParams.MAX_AGE);
                            return config;
                        }))
                .authorizeHttpRequests(c ->
                        c.requestMatchers("/login").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/v1/delete-user/**").hasAuthority(ADMIN)
                                .requestMatchers(HttpMethod.DELETE, "/v1/delete-role/**").hasAuthority(ADMIN)

                                .requestMatchers(HttpMethod.PUT, "/v1/update-user/**").hasAnyAuthority(ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/v1/add-role-to-user/**").hasAnyAuthority(ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/v1/remove-role-to-user/**").hasAnyAuthority(ADMIN)

                                .requestMatchers(HttpMethod.POST, "/v1/create-user/**").hasAnyAuthority(ADMIN)
                                .requestMatchers(HttpMethod.POST, "/v1/create-role/**").hasAnyAuthority(ADMIN)

                                .requestMatchers(HttpMethod.GET, "/v1/get-role/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/v1/get-user/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/v1/find-user/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(HttpMethod.GET, "/v1/list-users/**").hasAnyAuthority(USER, ADMIN)
                                .anyRequest().authenticated());
        http.addFilterBefore(new JWTAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JWTAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
