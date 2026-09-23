package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.request.ServicioInicioItemRequest;
import com.pharma.consultoria_pharma.dto.response.ServicioResponse;
import com.pharma.consultoria_pharma.services.FileStorageService;
import com.pharma.consultoria_pharma.services.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/admin/servicios")
@RequiredArgsConstructor
public class ServicioAdminController {

    private final ServicioService servicioService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<Page<ServicioResponse>> listar(
            @PageableDefault(size = 10, sort = "idServicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(servicioService.listar(pageable));
    }

    @GetMapping("/inicio")
    public ResponseEntity<List<ServicioResponse>> listarConfiguracionInicio() {
        return ResponseEntity.ok(servicioService.listarConfiguracionInicio());
    }

    @PutMapping("/inicio")
    public ResponseEntity<Void> actualizarConfiguracionInicio(
            @RequestBody List<@Valid ServicioInicioItemRequest> items) {
        servicioService.actualizarConfiguracionInicio(items);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(request));
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServicioResponse> crear(
            @Valid @ModelAttribute ServicioRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        String imageUrl = fileStorageService.storeFile(imageFile);
        if (imageUrl != null) {
            request.setImagen(imageUrl);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @PutMapping(path = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute ServicioRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        String imageUrl = fileStorageService.storeFile(imageFile);
        if (imageUrl != null) {
            request.setImagen(imageUrl);
        }
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
