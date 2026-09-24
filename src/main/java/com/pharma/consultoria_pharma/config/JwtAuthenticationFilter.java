package com.pharma.consultoria_pharma.config;

import com.pharma.consultoria_pharma.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
            reject(response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                reject(response);
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (!jwtUtil.isTokenValid(token, userDetails)) {
                reject(response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.debug("No se pudo validar el JWT: {}", e.getClass().getSimpleName());
            SecurityContextHolder.clearContext();
            reject(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void reject(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Token JWT inválido, expirado o usuario desactivado\"}");
    }
}
