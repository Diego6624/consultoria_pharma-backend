package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.request.ServicioInicioItemRequest;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import com.pharma.consultoria_pharma.entities.Categoria;
import com.pharma.consultoria_pharma.entities.Servicio;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.CategoriaRepository;
import com.pharma.consultoria_pharma.repositories.ServicioRepository;
import com.pharma.consultoria_pharma.services.ServicioService;
import com.pharma.consultoria_pharma.utils.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final CategoriaRepository categoriaRepository;
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
    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorSlug(String slug) {
        return mapper.toServicioResponse(servicioRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado: " + slug)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarParaInicio(int limite) {
        int limiteSeguro = Math.max(1, Math.min(limite, 3));
        return servicioRepository
                .findByMostrarEnInicioTrueOrderByOrdenInicioAscIdServicioDesc(PageRequest.of(0, limiteSeguro))
                .stream()
                .map(mapper::toServicioResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarConfiguracionInicio() {
        return servicioRepository.findAll(Sort.by(Sort.Direction.DESC, "idServicio"))
                .stream()
                .map(mapper::toServicioResponse)
                .toList();
    }

    @Override
    @Transactional
    public void actualizarConfiguracionInicio(List<ServicioInicioItemRequest> items) {
        if (items == null || items.size() > 3) {
            throw new IllegalArgumentException("Solo puedes seleccionar hasta 3 servicios para Inicio");
        }

        Set<Long> ids = new HashSet<>();
        Set<Integer> ordenes = new HashSet<>();
        servicioRepository.findAll().forEach(servicio -> {
            servicio.setMostrarEnInicio(false);
            servicio.setOrdenInicio(null);
            servicioRepository.save(servicio);
        });

        for (ServicioInicioItemRequest item : items) {
            if (!ids.add(item.getIdServicio()) || !ordenes.add(item.getOrdenInicio())) {
                throw new IllegalArgumentException("Los servicios y sus posiciones no pueden repetirse");
            }
            Servicio servicio = findById(item.getIdServicio());
            servicio.setMostrarEnInicio(true);
            servicio.setOrdenInicio(item.getOrdenInicio());
            servicio.setIconoInicio(item.getIconoInicio() == null || item.getIconoInicio().isBlank()
                    ? "ClipboardCheck"
                    : item.getIconoInicio());
            servicioRepository.save(servicio);
        }
    }

    @Override
    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = mapper.toServicio(request);
        servicio.setSlug(uniqueSlug(request.getTitulo(), null));
        aplicarConfiguracionInicio(servicio, request, true);
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        servicio.setCategoria(categoria);
        return mapper.toServicioResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse actualizar(Long id, ServicioRequest request) {
        Servicio servicio = findById(id);
        mapper.updateServicio(request, servicio);
        servicio.setSlug(uniqueSlug(request.getTitulo(), servicio.getIdServicio()));
        aplicarConfiguracionInicio(servicio, request, false);
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        servicio.setCategoria(categoria);
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

    private String uniqueSlug(String titulo, Long idServicio) {
        String baseSlug = SlugUtil.generate(titulo);
        if (baseSlug.isBlank()) {
            baseSlug = "servicio";
        }
        String slug = baseSlug;
        int counter = 1;
        while (idServicio == null
                ? servicioRepository.existsBySlug(slug)
                : servicioRepository.existsBySlugAndIdServicioNot(slug, idServicio)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }

    private void aplicarConfiguracionInicio(Servicio servicio, ServicioRequest request, boolean defaults) {
        if (defaults || request.getMostrarEnInicio() != null) {
            servicio.setMostrarEnInicio(Boolean.TRUE.equals(request.getMostrarEnInicio()));
        }
        if (defaults || request.getOrdenInicio() != null || Boolean.FALSE.equals(request.getMostrarEnInicio())) {
            servicio.setOrdenInicio(request.getOrdenInicio());
        }
        if (defaults || request.getIconoInicio() != null) {
            servicio.setIconoInicio(request.getIconoInicio() == null || request.getIconoInicio().isBlank()
                    ? "ClipboardCheck"
                    : request.getIconoInicio());
        }
    }
}
