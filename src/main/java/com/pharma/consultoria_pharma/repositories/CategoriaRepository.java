package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Categoria;
import com.pharma.consultoria_pharma.entities.TipoCategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findByTipo(TipoCategoria tipo);
    Page<Categoria> findByTipo(TipoCategoria tipo, Pageable pageable);
    boolean existsByNombre(String nombre);
}
