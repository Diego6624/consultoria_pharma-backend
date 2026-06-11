package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.UsuarioRequest;
import com.pharma.consultoria_pharma.dto.request.UsuarioUpdateRequest;
import com.pharma.consultoria_pharma.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Page<UsuarioResponse> listar(Pageable pageable, Boolean estado);

    UsuarioResponse obtenerPorId(Long id);

    UsuarioResponse crear(UsuarioRequest request);

    UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request);

    void eliminar(Long id);

    UsuarioResponse cambiarEstado(Long id, boolean activo);
}
