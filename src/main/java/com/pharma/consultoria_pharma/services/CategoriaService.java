package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.CategoriaRequest;
import com.pharma.consultoria_pharma.dto.response.CategoriaResponse;
import com.pharma.consultoria_pharma.entities.TipoCategoria;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {

    List<CategoriaResponse> listar();

    List<CategoriaResponse> listarPorTipo(TipoCategoria tipo);

    Page<CategoriaResponse> listar(Pageable pageable, TipoCategoria tipo);

    CategoriaResponse obtenerPorId(Long id);

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void eliminar(Long id);
}
