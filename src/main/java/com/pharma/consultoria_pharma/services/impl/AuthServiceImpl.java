package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.config.JwtUtil;
import com.pharma.consultoria_pharma.dto.request.LoginRequest;
import com.pharma.consultoria_pharma.dto.response.AuthResponse;
import com.pharma.consultoria_pharma.entities.Usuario;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.repositories.UsuarioRepository;
import com.pharma.consultoria_pharma.services.AuthService;
import com.pharma.consultoria_pharma.security.LoginRateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final LoginRateLimiter loginRateLimiter;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request, String clientIp) {
        String normalizedEmail = request.getEmail() == null
                ? ""
                : request.getEmail().trim().toLowerCase(Locale.ROOT);
        String ipKey = "ip:" + clientIp;
        String emailKey = "email:" + normalizedEmail;
        loginRateLimiter.check(ipKey);
        loginRateLimiter.check(emailKey);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException exception) {
            loginRateLimiter.recordFailure(ipKey);
            loginRateLimiter.recordFailure(emailKey);
            throw exception;
        }

        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().getNombre());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().getNombre())
                .build();
        loginRateLimiter.reset(ipKey);
        loginRateLimiter.reset(emailKey);
        return response;
    }
}
