package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.ConsultaRequest;
import com.pharma.consultoria_pharma.dto.response.ConsultaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConsultaService {

    ConsultaResponse crear(ConsultaRequest request);

    Page<ConsultaResponse> listar(Pageable pageable);

    ConsultaResponse obtenerPorId(Long id);

    ConsultaResponse marcarRevisada(Long id);

    ConsultaResponse actualizarRevision(Long id, boolean revisada);

    void eliminar(Long id);

    List<ConsultaResponse> listarTodas();
}
