package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.CategoriaRequest;
import com.pharma.consultoria_pharma.dto.response.CategoriaResponse;
import com.pharma.consultoria_pharma.entities.Categoria;
import com.pharma.consultoria_pharma.exceptions.BusinessException;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.CategoriaRepository;
import com.pharma.consultoria_pharma.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(mapper::toCategoriaResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id) {
        return mapper.toCategoriaResponse(findById(id));
    }

    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("Ya existe una categoría con ese nombre");
        }
        Categoria categoria = mapper.toCategoria(request);
        return mapper.toCategoriaResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = findById(id);
        categoria.setNombre(request.getNombre());
        return mapper.toCategoriaResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = findById(id);
        if (!categoria.getNoticias().isEmpty()) {
            throw new BusinessException("No se puede eliminar una categoría con noticias asociadas");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }
}
