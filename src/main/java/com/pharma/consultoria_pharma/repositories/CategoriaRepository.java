package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Categoria;
import com.pharma.consultoria_pharma.entities.TipoCategoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findByTipo(TipoCategoria tipo);
    boolean existsByNombre(String nombre);
}
