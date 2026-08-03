package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.response.NoticiaResponse;
import com.pharma.consultoria_pharma.services.FileStorageService;
import com.pharma.consultoria_pharma.services.NoticiaService;
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

@RestController
@RequestMapping("/api/admin/noticias")
@RequiredArgsConstructor
public class NoticiaAdminController {

    private final NoticiaService noticiaService;
    private final FileStorageService fileStorageService;

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

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoticiaResponse> crear(
            @Valid @ModelAttribute NoticiaRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        String imageUrl = fileStorageService.storeFile(imageFile);
        if (imageUrl != null) {
            request.setImagen(imageUrl);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(noticiaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticiaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody NoticiaRequest request) {
        return ResponseEntity.ok(noticiaService.actualizar(id, request));
    }

    @PutMapping(path = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoticiaResponse> actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute NoticiaRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        String imageUrl = fileStorageService.storeFile(imageFile);
        if (imageUrl != null) {
            request.setImagen(imageUrl);
        }
        return ResponseEntity.ok(noticiaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        noticiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
