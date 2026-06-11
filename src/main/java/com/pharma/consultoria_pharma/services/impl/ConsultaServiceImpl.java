package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.ConsultaRequest;
import com.pharma.consultoria_pharma.dto.response.ConsultaResponse;
import com.pharma.consultoria_pharma.entities.Consulta;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.ConsultaRepository;
import com.pharma.consultoria_pharma.services.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional
    public ConsultaResponse crear(ConsultaRequest request) {
        Consulta consulta = mapper.toConsulta(request);
        consulta.setFecha(LocalDateTime.now());
        return mapper.toConsultaResponse(consultaRepository.save(consulta));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultaResponse> listar(Pageable pageable) {
        return consultaRepository.findAll(pageable).map(mapper::toConsultaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultaResponse obtenerPorId(Long id) {
        return mapper.toConsultaResponse(findById(id));
    }

    private Consulta findById(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta no encontrada con id: " + id));
    }
}
