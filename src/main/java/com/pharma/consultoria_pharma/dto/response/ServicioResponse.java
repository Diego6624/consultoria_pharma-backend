package com.pharma.consultoria_pharma.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponse {

    private Long idServicio;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
