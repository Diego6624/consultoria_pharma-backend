package com.pharma.consultoria_pharma.controllers;

import com.pharma.consultoria_pharma.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = fileStorageService.storeFile(file);
        if (imageUrl == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo subir la imagen"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("url", imageUrl));
    }
}
