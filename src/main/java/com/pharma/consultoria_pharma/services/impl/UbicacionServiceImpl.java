package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.UbicacionRequest;
import com.pharma.consultoria_pharma.dto.response.UbicacionResponse;
import com.pharma.consultoria_pharma.entities.Ubicacion;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.UbicacionRepository;
import com.pharma.consultoria_pharma.services.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UbicacionResponse> listar(Pageable pageable) {
        return ubicacionRepository.findAll(pageable).map(mapper::toUbicacionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UbicacionResponse obtenerPorId(Long id) {
        return mapper.toUbicacionResponse(findById(id));
    }

    @Override
    @Transactional
    public UbicacionResponse crear(UbicacionRequest request) {
        Ubicacion ubicacion = mapper.toUbicacion(request);
        return mapper.toUbicacionResponse(ubicacionRepository.save(ubicacion));
    }

    @Override
    @Transactional
    public UbicacionResponse actualizar(Long id, UbicacionRequest request) {
        Ubicacion ubicacion = findById(id);
        mapper.updateUbicacion(request, ubicacion);
        return mapper.toUbicacionResponse(ubicacionRepository.save(ubicacion));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ubicacionRepository.delete(findById(id));
    }

    private Ubicacion findById(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con id: " + id));
    }
}
