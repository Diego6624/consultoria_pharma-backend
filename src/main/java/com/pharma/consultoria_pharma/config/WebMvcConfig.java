package com.pharma.consultoria_pharma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.CacheControl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.domain.PageRequest;

import java.nio.file.Path;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final int MAX_PAGE_SIZE = 100;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return resolver -> {
            resolver.setMaxPageSize(MAX_PAGE_SIZE);
            resolver.setOneIndexedParameters(false);
            resolver.setFallbackPageable(PageRequest.of(0, 10));
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(uploadPath.toUri().toString())
            .setCacheControl(CacheControl.maxAge(java.time.Duration.ofDays(30)).cachePublic());
    }
}
