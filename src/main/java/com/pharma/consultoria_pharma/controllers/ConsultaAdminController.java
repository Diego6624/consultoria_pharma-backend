package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.dto.response.ConsultaResponse;
import com.pharma.consultoria_pharma.services.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/consultas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
public class ConsultaAdminController {

    private final ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<Page<ConsultaResponse>> listar(
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/revisada")
    public ResponseEntity<ConsultaResponse> marcarRevisada(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.marcarRevisada(id));
    }

    @PatchMapping("/{id}/revision")
    public ResponseEntity<ConsultaResponse> actualizarRevision(
            @PathVariable Long id,
            @RequestParam boolean revisada) {
        return ResponseEntity.ok(consultaService.actualizarRevision(id, revisada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        consultaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<List<ConsultaResponse>> exportar() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }
}
