package com.eugeniaArias.spring_security_jwt.security.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eugeniaArias.spring_security_jwt.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


public class JwtValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public JwtValidator(JwtUtils jwtUtils) {
        this.jwtUtils =jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean shouldSkip = path.startsWith("/auth/")
                || path.equals("/error")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/actuator/health");

        System.out.println("Path: " + path + ", Should skip: " + shouldSkip); // Debug
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si no hay Authorization o no comienza con "Bearer " → seguir sin autenticar
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // quitar "Bearer "

        try {
            DecodedJWT decoded = jwtUtils.validateToken(token);

            String username = jwtUtils.extractUsername(decoded);
            String authorities = jwtUtils.extractSpecificClaim(decoded, "authorities").asString();

            var granted = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            var auth = new UsernamePasswordAuthenticationToken(username, null, granted);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"invalid_token\",\"message\":\"" + ex.getMessage() + "\"}");

        }
    }

}

