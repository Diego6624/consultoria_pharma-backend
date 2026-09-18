package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.response.NoticiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticiaService {

    Page<NoticiaResponse> listar(Pageable pageable, Long idCategoria);

    NoticiaResponse obtenerPorId(Long id);

    NoticiaResponse obtenerPorSlug(String slug);

    NoticiaResponse crear(NoticiaRequest request);

    NoticiaResponse actualizar(Long id, NoticiaRequest request);

    void eliminar(Long id);
}
