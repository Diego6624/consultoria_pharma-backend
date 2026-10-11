package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.entities.StoredImage;
import com.pharma.consultoria_pharma.repositories.StoredImageRepository;
import com.pharma.consultoria_pharma.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final StoredImageRepository storedImageRepository;

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String imageId = UUID.randomUUID().toString();
            StoredImage image = StoredImage.builder()
                    .id(imageId)
                    .filename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            storedImageRepository.save(image);

            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(imageId)
                    .toUriString();
        } catch (IOException ex) {
            throw new UncheckedIOException("No se pudo leer la imagen subida", ex);
        }
    }

    @Override
    public Optional<StoredImage> findFileById(String id) {
        return storedImageRepository.findById(id);
    }
}
