package com.mounanga.customerqueryservice.security;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for setting up Spring Security with Keycloak authentication.
 * <p>
 * This configuration:
 * <ul>
 *     <li>Disables CSRF protection for simplicity.</li>
 *     <li>Allows access to Actuator endpoints.</li>
 *     <li>Requires authentication for all other requests.</li>
 *     <li>Configures stateless session management.</li>
 *     <li>Integrates OAuth2 resource server with a custom JWT authentication converter.</li>
 * </ul>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final KeycloakJwtAuthenticationConverter converter;

    /**
     * Constructs the SecurityConfiguration with the required Keycloak JWT Authentication Converter.
     *
     * @param converter the Keycloak-specific JWT authentication converter
     */
    public SecurityConfiguration(KeycloakJwtAuthenticationConverter converter) {
        this.converter = converter;
    }

    /**
     * Configures the security filter chain.
     *
     * @param httpSecurity the {@link HttpSecurity} object to configure security settings
     * @return the configured {@link SecurityFilterChain}
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .anyRequest().authenticated());

        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return httpSecurity.build();
    }


    /**
     * Returns a list of endpoints that are publicly accessible (no authentication required).
     *
     * @return array of public URL patterns
     */
    @Contract(value = " -> new", pure = true)
    private String @NotNull [] getPublicEndpoints() {
        return new String[]{
                "/actuator/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/api/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        };
    }
}

