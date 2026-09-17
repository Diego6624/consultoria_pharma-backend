package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	List<Consulta> findTop5ByOrderByFechaDesc();

	long countByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

	long countByFechaBetweenAndRevisadaFalse(LocalDateTime inicio, LocalDateTime fin);
}
