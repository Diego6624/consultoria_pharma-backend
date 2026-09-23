package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.request.ServicioInicioItemRequest;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ServicioService {

    Page<ServicioResponse> listar(Pageable pageable);

    ServicioResponse obtenerPorId(Long id);

    ServicioResponse obtenerPorSlug(String slug);

    List<ServicioResponse> listarParaInicio(int limite);

    List<ServicioResponse> listarConfiguracionInicio();

    void actualizarConfiguracionInicio(List<ServicioInicioItemRequest> items);

    ServicioResponse crear(ServicioRequest request);

    ServicioResponse actualizar(Long id, ServicioRequest request);

    void eliminar(Long id);
}
