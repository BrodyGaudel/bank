package com.mounanga.customerqueryservice.security;


import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * Configuration class for setting up auditing in the application.
 *
 * <p>
 * This class defines the {@link AuditorAware} bean used by Spring Data JPA
 * to automatically populate the auditing fields such as {@code createdBy}
 * and {@code lastModifiedBy}.
 * </p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Configuration
public class AuditAwareConfiguration {

    /**
     * Creates an {@link AuditorAware} bean that provides the current auditor's username.
     *
     * @return an instance of AuditorAwareImpl.
     */
    @Bean
    public AuditorAware<@NotNull String> auditorAware() {
        return new AuditAwareImpl();
    }
}


