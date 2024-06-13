package org.mounanga.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.userservice.util.ApplicationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JWTAuthorizationFilterTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.setContext(new SecurityContextImpl());
        this.jwtAuthorizationFilter = new JWTAuthorizationFilter(applicationProperties);
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws Exception {
        request.setServletPath("/test");
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() {
        when(applicationProperties.getJwtSecret()).thenReturn("secret");
        request.addHeader("Authorization", "Bearer invalid-token");
        assertThrows(JWTDecodeException.class, () ->  jwtAuthorizationFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    void testDoFilterInternalWithValidToken() throws Exception {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");

        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(algorithm);

        when(applicationProperties.getJwtSecret()).thenReturn("secret");
        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}