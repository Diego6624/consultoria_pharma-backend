package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {

    Page<Noticia> findByCategoriaIdCategoria(Long idCategoria, Pageable pageable);
}
