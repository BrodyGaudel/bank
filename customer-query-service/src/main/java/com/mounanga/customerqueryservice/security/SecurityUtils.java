package com.mounanga.customerqueryservice.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;


@Slf4j
public final class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérification de l'authentification
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("No authentication found in security context");
            return StringUtils.EMPTY;
        }

        // Vérification des authentifications anonymes
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.debug("Anonymous authentication detected");
            return StringUtils.EMPTY;
        }

        Object principal = authentication.getPrincipal();

        // Cas où principal est null (normalement ne devrait pas arriver si authentifié)
        switch (principal) {
            case null -> {
                log.warn("Authentication principal is null");
                return StringUtils.EMPTY;
            }
            case UserDetails userDetails -> {
                return extractUsernameFromUserDetails(userDetails);
            }
            case Jwt jwt -> {
                return extractUsernameFromJwt(jwt);
            }
            default -> {
                return extractUsernameFromOtherPrincipal(principal);
            }
        }

    }

    private static String extractUsernameFromUserDetails(UserDetails userDetails) {
        try {
            return userDetails.getUsername();
        } catch (Exception e) {
            log.warn("Failed to extract username from UserDetails", e);
            return StringUtils.EMPTY;
        }
    }

    private static String extractUsernameFromJwt(Jwt jwt) {
        try {
            // Ordre de priorité pour l'extraction du username
            String[] claimNames = {"preferred_username", "email", "sub", "user_name", "username"};

            for (String claimName : claimNames) {
                String claimValue = jwt.getClaim(claimName);
                if (StringUtils.isNotBlank(claimValue)) {
                    log.debug("Extracted username from JWT claim '{}'", claimName);
                    return claimValue;
                }
            }

            // Fallback: subject du JWT
            String subject = jwt.getSubject();
            if (StringUtils.isNotBlank(subject)) {
                log.debug("Using JWT subject as username");
                return subject;
            }

            log.warn("No valid username claim found in JWT token");
            return StringUtils.EMPTY;

        } catch (Exception e) {
            log.warn("Failed to extract username from JWT", e);
            return StringUtils.EMPTY;
        }
    }

    private static String extractUsernameFromOtherPrincipal(Object principal) {
        try {
            // Tentative d'extraction via toString()
            String username = principal.toString();

            // Vérification que ce n'est pas une représentation par défaut
            if (StringUtils.isNotBlank(username) &&
                    !username.startsWith("org.springframework") &&
                    !username.contains("@")) {
                log.debug("Using principal.toString() as username: {}", username);
                return username;
            }

            log.warn("Principal is of unsupported type: {}", principal.getClass().getName());
            return StringUtils.EMPTY;

        } catch (Exception e) {
            log.warn("Failed to extract username from principal", e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * Méthode utilitaire pour vérifier si l'utilisateur est authentifié
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Méthode utilitaire pour obtenir l'authentification complète
     */
    public static @NotNull Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}