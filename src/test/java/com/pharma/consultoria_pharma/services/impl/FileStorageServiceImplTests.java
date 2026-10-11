package com.pharma.consultoria_pharma.services.impl;

import com.pharma.consultoria_pharma.entities.StoredImage;
import com.pharma.consultoria_pharma.repositories.StoredImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceImplTests {

    @Mock
    private StoredImageRepository storedImageRepository;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void storeFilePersistsImageBytesAndReturnsImageEndpointUrl() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("consultoria.test");
        request.setServerPort(443);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        byte[] imageBytes = {1, 2, 3};
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", imageBytes);
        when(storedImageRepository.save(any(StoredImage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String imageUrl = fileStorageService.storeFile(file);

        ArgumentCaptor<StoredImage> imageCaptor = ArgumentCaptor.forClass(StoredImage.class);
        verify(storedImageRepository).save(imageCaptor.capture());
        StoredImage storedImage = imageCaptor.getValue();
        assertArrayEquals(imageBytes, storedImage.getData());
        assertEquals("image.png", storedImage.getFilename());
        assertEquals("image/png", storedImage.getContentType());
        assertEquals("https://consultoria.test/api/images/" + storedImage.getId(), imageUrl);
    }

    @Test
    void storeFileDoesNotSaveAnEmptyFile() {
        assertNull(fileStorageService.storeFile(new MockMultipartFile("file", new byte[0])));

        verify(storedImageRepository, never()).save(any(StoredImage.class));
    }

    @Test
    void findFileByIdReadsFromRepository() {
        StoredImage image = StoredImage.builder().id("image-id").build();
        when(storedImageRepository.findById("image-id")).thenReturn(Optional.of(image));

        assertEquals(Optional.of(image), fileStorageService.findFileById("image-id"));
    }
}
