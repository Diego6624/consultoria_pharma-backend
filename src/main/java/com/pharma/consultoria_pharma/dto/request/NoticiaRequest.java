package com.pharma.consultoria_pharma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiaRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500)
    private String descripcion;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 100000, message = "El contenido no puede superar los 100000 caracteres")
    private String contenido;

    @Size(max = 500)
    private String imagen;

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoría debe ser válida")
    private Long idCategoria;
}
