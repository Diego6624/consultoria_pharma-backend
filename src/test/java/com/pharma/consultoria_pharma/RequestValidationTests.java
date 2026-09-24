package com.pharma.consultoria_pharma;

import com.pharma.consultoria_pharma.dto.request.ConsultaRequest;
import com.pharma.consultoria_pharma.dto.request.LoginRequest;
import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void consultaRechazaTelefonoInvalidoYServicioNegativo() {
        ConsultaRequest request = ConsultaRequest.builder()
                .nombre("Usuario de prueba")
                .correo("usuario@example.com")
                .telefono("telefono-invalido")
                .mensaje("Necesito información")
                .idServicio(-1L)
                .build();

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void noticiaRechazaContenidoExcesivamenteGrande() {
        NoticiaRequest request = NoticiaRequest.builder()
                .titulo("Noticia válida")
                .descripcion("Descripción válida")
                .contenido("x".repeat(100001))
                .idCategoria(1L)
                .build();

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void servicioAceptaSolicitudValida() {
        ServicioRequest request = ServicioRequest.builder()
                .titulo("Servicio válido")
                .descripcion("Descripción válida")
                .contenido("Contenido válido")
                .idCategoria(1L)
                .ordenInicio(1)
                .build();

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void loginRechazaContrasenaExcesivamenteGrande() {
        LoginRequest request = LoginRequest.builder()
                .email("usuario@example.com")
                .password("x".repeat(129))
                .build();

        assertFalse(validator.validate(request).isEmpty());
    }
}
