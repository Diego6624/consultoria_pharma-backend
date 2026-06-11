package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.dto.request.CategoriaRequest;
import com.pharma.consultoria_pharma.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> listar();

    CategoriaResponse obtenerPorId(Long id);

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void eliminar(Long id);
}
