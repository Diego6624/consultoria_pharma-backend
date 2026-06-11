package com.pharma.consultoria_pharma.config;

import com.pharma.consultoria_pharma.entities.Rol;
import com.pharma.consultoria_pharma.entities.Usuario;
import com.pharma.consultoria_pharma.repositories.RolRepository;
import com.pharma.consultoria_pharma.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.master.email}")
    private String masterEmail;

    @Value("${app.master.password}")
    private String masterPassword;

    @Value("${app.master.nombre}")
    private String masterNombre;

    @Override
    public void run(String... args) {
        initRoles();
        initMasterUser();
    }

    private void initRoles() {
        createRoleIfNotExists("ROLE_MASTER");
        createRoleIfNotExists("ROLE_ADMIN");
    }

    private void createRoleIfNotExists(String nombre) {
        if (rolRepository.findByNombre(nombre).isEmpty()) {
            rolRepository.save(Rol.builder().nombre(nombre).build());
            log.info("Rol creado: {}", nombre);
        }
    }

    private void initMasterUser() {
        if (usuarioRepository.findByEmail(masterEmail).isPresent()) {
            return;
        }

        Rol rolMaster = rolRepository.findByNombre("ROLE_MASTER")
                .orElseThrow(() -> new IllegalStateException("Rol ROLE_MASTER no encontrado"));

        Usuario master = Usuario.builder()
                .nombre(masterNombre)
                .email(masterEmail)
                .password(passwordEncoder.encode(masterPassword))
                .estado(true)
                .rol(rolMaster)
                .build();

        usuarioRepository.save(master);
        log.info("Usuario MASTER inicial creado: {}", masterEmail);
    }
}
