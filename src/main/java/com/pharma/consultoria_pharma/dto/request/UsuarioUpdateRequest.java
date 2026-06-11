package com.pharma.consultoria_pharma.dto.request;

import jakarta.validation.constraints.Email;
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
public class UsuarioUpdateRequest {

    @Size(max = 100)
    private String nombre;

    @Email
    @Size(max = 150)
    private String email;

    @Size(min = 8)
    private String password;

    private String rol;
}
