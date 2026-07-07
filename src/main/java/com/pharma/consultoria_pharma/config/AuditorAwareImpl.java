package com.pharma.consultoria_pharma.config;

import com.pharma.consultoria_pharma.entities.Usuario;
import com.pharma.consultoria_pharma.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Usuario> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return Optional.empty();
        }

        // getReference no ejecuta SELECT: evita recursión infinita con JPA Auditing en PUT/PATCH
        return Optional.of(entityManager.getReference(Usuario.class, userDetails.getIdUsuario()));
    }
}
