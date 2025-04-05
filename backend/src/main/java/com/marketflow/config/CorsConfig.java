package com.marketflow.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // или конкретные origin'ы
                .allowedMethods("*")
                .allowedHeaders("*")  // <-- тут
                .exposedHeaders("*"); // <-- и это тоже
    }
}