package com.pharma.consultoria_pharma.security;

import com.pharma.consultoria_pharma.entities.Usuario;
import com.pharma.consultoria_pharma.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new CustomUserDetails(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getPassword(),
                Boolean.TRUE.equals(usuario.getEstado()),
                List.of(new SimpleGrantedAuthority(usuario.getRol().getNombre()))
        );
    }
}
