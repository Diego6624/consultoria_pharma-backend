package com.pharma.consultoria_pharma.services;

import com.pharma.consultoria_pharma.entities.StoredImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    Optional<StoredImage> findFileById(String id);
}
