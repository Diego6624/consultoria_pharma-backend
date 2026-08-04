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
public class ConsultaResponse {

    private Long idConsulta;
    private String nombre;
    private String correo;
    private String telefono;
    private String mensaje;
    private Long idServicio;
    private LocalDateTime fecha;
}
