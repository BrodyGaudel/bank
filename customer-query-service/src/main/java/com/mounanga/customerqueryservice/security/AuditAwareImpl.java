package com.mounanga.customerqueryservice.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} to provide the current auditor's username.
 *
 * <p>
 * This class is used by Spring Data JPA's auditing feature to automatically fill
 * audit fields such as {@code createdBy} and {@code lastModifiedBy} in entities.
 * </p>
 *
 * <p>
 * It retrieves the current user's username from the {@link SecurityUtils} helper class,
 * which fetches the authentication details from the Spring Security context.
 * </p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Component
public class AuditAwareImpl implements AuditorAware<@NotNull String> {

    /**
     * Retrieves the current auditor's username from the security context.
     *
     * @return an {@link Optional} containing the username, or an empty {@link Optional} if no user is authenticated.
     */
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUsername());
    }
}

