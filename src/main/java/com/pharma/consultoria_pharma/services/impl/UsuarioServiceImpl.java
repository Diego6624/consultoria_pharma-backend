package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.UsuarioRequest;
import com.pharma.consultoria_pharma.dto.request.UsuarioUpdateRequest;
import com.pharma.consultoria_pharma.dto.response.UsuarioResponse;
import com.pharma.consultoria_pharma.entities.Rol;
import com.pharma.consultoria_pharma.entities.Usuario;
import com.pharma.consultoria_pharma.exceptions.BusinessException;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.RolRepository;
import com.pharma.consultoria_pharma.repositories.UsuarioRepository;
import com.pharma.consultoria_pharma.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(Pageable pageable, Boolean estado) {
        Page<Usuario> page = estado != null
                ? usuarioRepository.findByEstado(estado, pageable)
                : usuarioRepository.findAll(pageable);
        return page.map(mapper::toUsuarioResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return mapper.toUsuarioResponse(findById(id));
    }

    @Override
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        Rol rol = obtenerRolAdmin(request.getRol());

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .estado(true)
                .rol(rol)
                .creadoPor(obtenerUsuarioAutenticado())
                .build();

        return mapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = findById(id);

        if ("ROLE_MASTER".equals(usuario.getRol().getNombre())) {
            throw new BusinessException("No se puede modificar un usuario MASTER");
        }

        if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("Ya existe un usuario con ese email");
            }
            usuario.setEmail(request.getEmail());
        }

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRol() != null) {
            usuario.setRol(obtenerRolAdmin(request.getRol()));
        }

        return mapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = findById(id);

        if ("ROLE_MASTER".equals(usuario.getRol().getNombre())) {
            throw new BusinessException("No se puede eliminar un usuario MASTER");
        }

        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstado(Long id, boolean activo) {
        Usuario usuario = findById(id);

        if ("ROLE_MASTER".equals(usuario.getRol().getNombre())) {
            throw new BusinessException("No se puede desactivar un usuario MASTER");
        }

        usuario.setEstado(activo);
        return mapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    private Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return usuarioRepository.findByEmail(authentication.getName()).orElse(null);
    }

    private Rol obtenerRolAdmin(String nombreRol) {
        if (!"ROLE_ADMIN".equals(nombreRol)) {
            throw new BusinessException("Solo se pueden crear usuarios con rol ROLE_ADMIN");
        }

        return rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombreRol));
    }
}
