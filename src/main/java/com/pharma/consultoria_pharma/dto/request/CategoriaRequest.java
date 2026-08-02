package com.pharma.consultoria_pharma.dto.request;

import com.pharma.consultoria_pharma.entities.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoriaRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "El tipo de categoría es obligatorio (NOTICIA o SERVICIO)")
    private TipoCategoria tipo;
}
