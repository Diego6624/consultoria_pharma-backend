package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.UbicacionRequest;
import com.pharma.consultoria_pharma.dto.response.UbicacionResponse;
import com.pharma.consultoria_pharma.services.UbicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ubicaciones")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
public class UbicacionAdminController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<Page<UbicacionResponse>> listar(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ubicacionService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ubicacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UbicacionResponse> crear(@Valid @RequestBody UbicacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UbicacionRequest request) {
        return ResponseEntity.ok(ubicacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
