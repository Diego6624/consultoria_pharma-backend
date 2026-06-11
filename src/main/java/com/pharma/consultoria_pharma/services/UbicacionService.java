package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.UbicacionRequest;
import com.pharma.consultoria_pharma.dto.response.UbicacionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UbicacionService {

    Page<UbicacionResponse> listar(Pageable pageable);

    UbicacionResponse obtenerPorId(Long id);

    UbicacionResponse crear(UbicacionRequest request);

    UbicacionResponse actualizar(Long id, UbicacionRequest request);

    void eliminar(Long id);
}
