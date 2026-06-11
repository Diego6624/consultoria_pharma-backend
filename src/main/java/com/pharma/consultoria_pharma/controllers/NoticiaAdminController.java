package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.response.NoticiaResponse;
import com.pharma.consultoria_pharma.services.NoticiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/noticias")
@RequiredArgsConstructor
public class NoticiaAdminController {

    private final NoticiaService noticiaService;

    @GetMapping
    public ResponseEntity<Page<NoticiaResponse>> listar(
            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long idCategoria) {
        return ResponseEntity.ok(noticiaService.listar(pageable, idCategoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticiaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(noticiaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<NoticiaResponse> crear(@Valid @RequestBody NoticiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticiaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticiaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody NoticiaRequest request) {
        return ResponseEntity.ok(noticiaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        noticiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
