package com.example.Chronicle;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This makes files in "C:/app-uploads/" accessible via the URL "/uploads/**"
        // The path here MUST match the one in your AppUtil.java
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/Projects/Chronicle/Chronicle/src/main/resources/static/uploads/");
    }
}