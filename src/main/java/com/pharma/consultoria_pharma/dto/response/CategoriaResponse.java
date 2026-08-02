package com.pharma.consultoria_pharma.dto.response;

import com.pharma.consultoria_pharma.entities.TipoCategoria;
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
public class CategoriaResponse {

    private Long idCategoria;
    private String nombre;
    private TipoCategoria tipo;
}
