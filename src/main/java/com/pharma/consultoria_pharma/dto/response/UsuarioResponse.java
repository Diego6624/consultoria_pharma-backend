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
public class UsuarioResponse {

    private Long idUsuario;
    private String nombre;
    private String email;
    private Boolean estado;
    private String rol;
    private String creadoPor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
