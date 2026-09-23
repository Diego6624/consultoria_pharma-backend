package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdServicioNot(String slug, Long idServicio);

    List<Servicio> findByMostrarEnInicioTrueOrderByOrdenInicioAscIdServicioDesc(Pageable pageable);
}
