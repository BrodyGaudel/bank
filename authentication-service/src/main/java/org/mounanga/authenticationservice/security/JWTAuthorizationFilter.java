package org.mounanga.authenticationservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.mounanga.authenticationservice.configuration.ApplicationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String ROLES = "roles";
    private static final String AUTHORIZATION = "Authorization";

    private final ApplicationProperties properties;

    public JWTAuthorizationFilter(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        if(header == null || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(properties.getJwtSecret())).build();
        String jwt= header.substring(7);
        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaims().get(ROLES).asList(String.class);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
