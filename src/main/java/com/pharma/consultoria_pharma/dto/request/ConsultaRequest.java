package com.pharma.consultoria_pharma.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
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
public class ConsultaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    @Size(max = 150)
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20)
    @Pattern(regexp = "^[0-9+()\\-.\\s]{7,20}$", message = "El teléfono no es válido")
    private String telefono;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 10000, message = "El mensaje no puede superar los 10000 caracteres")
    private String mensaje;

    @Positive(message = "El servicio debe ser válido")
    private Long idServicio;
}
