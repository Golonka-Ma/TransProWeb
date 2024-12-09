package com.example.transpro.config;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private final String secretKey;

    public JwtCookieAuthFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        if (request.getCookies() != null) {
            // Szukamy ciasteczka o nazwie "JWT_TOKEN"
            token = Arrays.stream(request.getCookies())
                    .filter(c -> "JWT_TOKEN".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst().orElse(null);
        }

        if (token != null) {
            try {
                var claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    // Zakładamy, że uprawnienia/role będą ładowane np. w UserDetailsService
                    var auth = new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // Token niepoprawny lub wygasł, można wyczyścić ciasteczko lub zwrócić 401
                // Ale tutaj po prostu nie ustawiamy auth
            }
        }

        filterChain.doFilter(request, response);
    }
}
