package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.CategoriaRequest;
import com.pharma.consultoria_pharma.dto.response.CategoriaResponse;
import com.pharma.consultoria_pharma.entities.TipoCategoria;
import com.pharma.consultoria_pharma.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar(
            @RequestParam(required = false) TipoCategoria tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(categoriaService.listarPorTipo(tipo));
        }
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CategoriaResponse>> listarPaginado(
            @RequestParam(required = false) TipoCategoria tipo,
            @PageableDefault(size = 10, sort = "idCategoria", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listar(pageable, tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
