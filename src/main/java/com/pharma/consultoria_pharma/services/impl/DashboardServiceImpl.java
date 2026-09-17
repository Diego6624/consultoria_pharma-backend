package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.response.DashboardResponse;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.CategoriaRepository;
import com.pharma.consultoria_pharma.repositories.ConsultaRepository;
import com.pharma.consultoria_pharma.repositories.NoticiaRepository;
import com.pharma.consultoria_pharma.repositories.ServicioRepository;
import com.pharma.consultoria_pharma.repositories.UbicacionRepository;
import com.pharma.consultoria_pharma.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ConsultaRepository consultaRepository;
    private final NoticiaRepository noticiaRepository;
    private final ServicioRepository servicioRepository;
    private final UbicacionRepository ubicacionRepository;
    private final CategoriaRepository categoriaRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerResumen() {
        LocalDate today = LocalDate.now();
        LocalDateTime inicioSemana = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime finSemana = today.plusDays(1).atStartOfDay();
        LocalDateTime inicioDia = today.atStartOfDay();
        LocalDateTime finDia = today.plusDays(1).atStartOfDay();

        var consultasRecientes = consultaRepository.findTop5ByOrderByFechaDesc();
        var articulosRecientes = noticiaRepository.findTop3ByOrderByFechaPublicacionDesc();

        return DashboardResponse.builder()
                .totalConsultas(consultaRepository.count())
                .totalArticulos(noticiaRepository.count())
                .totalServicios(servicioRepository.count())
                .totalUbicaciones(ubicacionRepository.count())
                .totalCategorias(categoriaRepository.count())
                .consultasEstaSemana(consultaRepository.countByFechaBetween(inicioSemana, finSemana))
                .consultasSinRevisarHoy(consultaRepository.countByFechaBetweenAndRevisadaFalse(inicioDia, finDia))
                .ultimaConsulta(consultasRecientes.isEmpty() ? null : mapper.toConsultaResponse(consultasRecientes.get(0)))
                .ultimoArticulo(articulosRecientes.isEmpty() ? null : mapper.toNoticiaResponse(articulosRecientes.get(0)))
                .consultasRecientes(consultasRecientes.stream().map(mapper::toConsultaResponse).toList())
                .articulosRecientes(articulosRecientes.stream().map(mapper::toNoticiaResponse).toList())
                .build();
    }
}
