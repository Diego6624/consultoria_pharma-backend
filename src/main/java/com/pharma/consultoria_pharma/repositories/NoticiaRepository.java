package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    Page<Noticia> findByCategoriaIdCategoria(Long idCategoria, Pageable pageable);

    List<Noticia> findTop3ByOrderByFechaPublicacionDesc();

    Optional<Noticia> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNoticiaNot(String slug, Long idNoticia);
}
