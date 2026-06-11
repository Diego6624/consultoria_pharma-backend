package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import com.pharma.consultoria_pharma.entities.Servicio;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.ServicioRepository;
import com.pharma.consultoria_pharma.services.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ServicioResponse> listar(Pageable pageable) {
        return servicioRepository.findAll(pageable).map(mapper::toServicioResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Long id) {
        return mapper.toServicioResponse(findById(id));
    }

    @Override
    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = mapper.toServicio(request);
        return mapper.toServicioResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse actualizar(Long id, ServicioRequest request) {
        Servicio servicio = findById(id);
        mapper.updateServicio(request, servicio);
        return mapper.toServicioResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        servicioRepository.delete(findById(id));
    }

    private Servicio findById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
    }
}
