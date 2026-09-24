package com.pharma.consultoria_pharma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ServicioRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 160, message = "La descripción no puede superar los 160 caracteres")
    private String descripcion;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 100000, message = "El contenido no puede superar los 100000 caracteres")
    private String contenido;

    @Size(max = 500)
    private String imagen;

    private Boolean mostrarEnInicio;

    @Min(value = 1, message = "El orden debe estar entre 1 y 3")
    @Max(value = 3, message = "El orden debe estar entre 1 y 3")
    private Integer ordenInicio;

    @Size(max = 80)
    private String iconoInicio;

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoría debe ser válida")
    private Long idCategoria;
}
