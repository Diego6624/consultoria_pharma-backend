package com.pharma.consultoria_pharma.repositories;

import com.pharma.consultoria_pharma.entities.StoredImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredImageRepository extends JpaRepository<StoredImage, String> {
}
