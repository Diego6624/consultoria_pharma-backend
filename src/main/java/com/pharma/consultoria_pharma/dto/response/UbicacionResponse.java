package com.pharma.consultoria_pharma.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionResponse {

    private Long idUbicacion;
    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
