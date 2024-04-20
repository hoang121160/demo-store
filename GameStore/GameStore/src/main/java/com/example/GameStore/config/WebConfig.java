package com.example.GameStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class WebConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        // Cấu hình kích thước tối đa của một file được tải lên
        resolver.setMaxUploadSize(10 * 1024 * 1024); // 10MB
        // Cấu hình kích thước tối đa của một request
        resolver.setMaxInMemorySize(10 * 1024 * 1024); // 10MB
        return resolver;
    }
}
