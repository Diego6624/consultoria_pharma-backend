package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.response.NoticiaResponse;
import com.pharma.consultoria_pharma.entities.Categoria;
import com.pharma.consultoria_pharma.entities.Noticia;
import com.pharma.consultoria_pharma.exceptions.ResourceNotFoundException;
import com.pharma.consultoria_pharma.mappers.EntityMapper;
import com.pharma.consultoria_pharma.repositories.CategoriaRepository;
import com.pharma.consultoria_pharma.repositories.NoticiaRepository;
import com.pharma.consultoria_pharma.services.NoticiaService;
import com.pharma.consultoria_pharma.utils.SlugUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticiaServiceImpl implements NoticiaService {

    private final NoticiaRepository noticiaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NoticiaResponse> listar(Pageable pageable, Long idCategoria) {
        Page<Noticia> page = idCategoria != null
                ? noticiaRepository.findByCategoriaIdCategoria(idCategoria, pageable)
                : noticiaRepository.findAll(pageable);
        return page.map(mapper::toNoticiaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticiaResponse obtenerPorId(Long id) {
        return mapper.toNoticiaResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public NoticiaResponse obtenerPorSlug(String slug) {
        return mapper.toNoticiaResponse(noticiaRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Noticia no encontrada: " + slug)));
    }

    @Override
    @Transactional
    public NoticiaResponse crear(NoticiaRequest request) {
        Noticia noticia = mapper.toNoticia(request);
        noticia.setSlug(uniqueSlug(request.getTitulo(), null));
        noticia.setFechaPublicacion(LocalDateTime.now());
        noticia.setCategoria(findCategoria(request.getIdCategoria()));
        return mapper.toNoticiaResponse(noticiaRepository.save(noticia));
    }

    @Override
    @Transactional
    public NoticiaResponse actualizar(Long id, NoticiaRequest request) {
        Noticia noticia = findById(id);
        mapper.updateNoticia(request, noticia);
        noticia.setSlug(uniqueSlug(request.getTitulo(), noticia.getIdNoticia()));
        noticia.setCategoria(findCategoria(request.getIdCategoria()));
        return mapper.toNoticiaResponse(noticiaRepository.save(noticia));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        noticiaRepository.delete(findById(id));
    }

    private Noticia findById(Long id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Noticia no encontrada con id: " + id));
    }

    private Categoria findCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    private String uniqueSlug(String titulo, Long idNoticia) {
        String baseSlug = SlugUtil.generate(titulo);
        if (baseSlug.isBlank()) {
            baseSlug = "noticia";
        }
        String slug = baseSlug;
        int counter = 1;
        while (idNoticia == null
                ? noticiaRepository.existsBySlug(slug)
                : noticiaRepository.existsBySlugAndIdNoticiaNot(slug, idNoticia)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }
}
