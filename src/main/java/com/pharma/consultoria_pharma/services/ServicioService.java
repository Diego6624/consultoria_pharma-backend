package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioService {

    Page<ServicioResponse> listar(Pageable pageable);

    ServicioResponse obtenerPorId(Long id);

    ServicioResponse crear(ServicioRequest request);

    ServicioResponse actualizar(Long id, ServicioRequest request);

    void eliminar(Long id);
}
