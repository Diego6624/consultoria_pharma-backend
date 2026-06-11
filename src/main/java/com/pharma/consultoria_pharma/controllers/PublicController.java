package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.ConsultaRequest;
import com.pharma.consultoria_pharma.dto.response.CategoriaResponse;
import com.pharma.consultoria_pharma.dto.response.ConsultaResponse;
import com.pharma.consultoria_pharma.dto.response.NoticiaResponse;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import com.pharma.consultoria_pharma.dto.response.UbicacionResponse;
import com.pharma.consultoria_pharma.services.CategoriaService;
import com.pharma.consultoria_pharma.services.ConsultaService;
import com.pharma.consultoria_pharma.services.NoticiaService;
import com.pharma.consultoria_pharma.services.ServicioService;
import com.pharma.consultoria_pharma.services.UbicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final NoticiaService noticiaService;
    private final ServicioService servicioService;
    private final UbicacionService ubicacionService;
    private final ConsultaService consultaService;
    private final CategoriaService categoriaService;

    @GetMapping("/noticias")
    public ResponseEntity<Page<NoticiaResponse>> listarNoticias(
            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long idCategoria) {
        return ResponseEntity.ok(noticiaService.listar(pageable, idCategoria));
    }

    @GetMapping("/noticias/{id}")
    public ResponseEntity<NoticiaResponse> obtenerNoticia(@PathVariable Long id) {
        return ResponseEntity.ok(noticiaService.obtenerPorId(id));
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/servicios")
    public ResponseEntity<Page<ServicioResponse>> listarServicios(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(servicioService.listar(pageable));
    }

    @GetMapping("/servicios/{id}")
    public ResponseEntity<ServicioResponse> obtenerServicio(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @GetMapping("/ubicaciones")
    public ResponseEntity<Page<UbicacionResponse>> listarUbicaciones(
            @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ubicacionService.listar(pageable));
    }

    @PostMapping("/consultas")
    public ResponseEntity<ConsultaResponse> enviarConsulta(@Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crear(request));
    }
}
