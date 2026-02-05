package com.mounanga.customerqueryservice.security;

import com.mounanga.customerqueryservice.util.ApplicationProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * Converts a {@link Jwt} token into an {@link AbstractAuthenticationToken} by extracting roles and authorities.
 * This converter is specifically designed for Keycloak authentication.
 * <p>
 * It retrieves roles from the "resource_access" claim and maps them to {@link GrantedAuthority} objects.
 * </p>
 * <p>
 * Usage: This component is used in Spring Security to convert JWTs into authentication tokens.
 * </p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<@NotNull Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(KeycloakJwtAuthenticationConverter.class);

    private static final String ROLES = "roles";
    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String PREFERRED_USERNAME = "preferred_username";

    private final ApplicationProperties properties;
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public KeycloakJwtAuthenticationConverter(ApplicationProperties properties) {
        this.properties = properties;
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    /**
     * Converts a {@link Jwt} token into an {@link AbstractAuthenticationToken}.
     * Extracts roles from the "resource_access" claim and maps them to {@link GrantedAuthority} objects.
     *
     * @param source the JWT token to convert (must not be {@code null})
     * @return an {@link AbstractAuthenticationToken} with extracted authorities
     */
    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt source) {
        log.info("Converting Jwt to AbstractAuthenticationToken");
        Collection<GrantedAuthority> authorities = Stream.concat(
                nullSafeStream(jwtGrantedAuthoritiesConverter.convert(source)),
                grantedAuthorities(source).stream()
        ).collect(Collectors.toSet());
        log.info("Authorities : {}", authorities);
        return new JwtAuthenticationToken(source, authorities, source.getClaim(PREFERRED_USERNAME));
    }

    /**
     * Extracts granted authorities from the given {@link Jwt} token.
     *
     * @param jwt the JWT token
     * @return a collection of granted authorities
     */
    private Collection<GrantedAuthority> grantedAuthorities(Jwt jwt) {
        List<String> roles = extractRoles(jwt);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    private @NotNull List<String> extractRoles(@NotNull Jwt jwt) {
        List<String> roles = new ArrayList<>();
        if (jwt.hasClaim(REALM_ACCESS)) {
            Object realmAccessObj = jwt.getClaim(REALM_ACCESS);
            extractAndAddRoles(roles, realmAccessObj);
        }
        if (jwt.hasClaim(RESOURCE_ACCESS)) {
            Object resourceAccessObj = jwt.getClaim(RESOURCE_ACCESS);
            if (resourceAccessObj instanceof Map) {

                @SuppressWarnings("unchecked")
                Map<String, Object> resourceAccess = (Map<String, Object>) resourceAccessObj;
                if (resourceAccess.containsKey(properties.getKeycloakClientId())) {
                    Object clientAccessObj = resourceAccess.get(properties.getKeycloakClientId());
                    extractAndAddRoles(roles, clientAccessObj);
                }
            }
        }
        log.debug("Rôles extracts : {}", roles);
        return roles;
    }

    private void extractAndAddRoles(List<String> roles, Object clientAccessObj) {
        if (clientAccessObj instanceof Map) {

            @SuppressWarnings("unchecked")
            Map<String, Object> clientAccess = (Map<String, Object>) clientAccessObj;
            Object clientRolesObj = clientAccess.get(ROLES);
            if (clientRolesObj instanceof List) {

                @SuppressWarnings("unchecked")
                List<String> clientRoles = (List<String>) clientRolesObj;
                roles.addAll(clientRoles);
            }
        }
    }


    /**
     * Converts a potentially {@code null} collection into a stream.
     *
     * @param collection the collection to convert (perhaps {@code null})
     * @param <T>        the type of elements in the collection
     * @return a {@link Stream} of the collection's elements, or an empty stream if the collection is {@code null}
     */
    private <T> Stream<T> nullSafeStream(Collection<T> collection) {
        return (collection == null) ? Stream.empty() : collection.stream();
    }
}




