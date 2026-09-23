package com.pharma.consultoria_pharma.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServicioInicioItemRequest {

    @NotNull
    private Long idServicio;

    @NotNull
    @Min(1)
    @Max(3)
    private Integer ordenInicio;

    @Size(max = 80)
    private String iconoInicio;
}
