package com.render.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig {

    @Value("${renderf.url.client}")
    private String clientUrl;

    @Value("${renderf.uri.task}")
    private String taskApi;

    @Value("${renderf.uri.customer}")
    private String customerApi;


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(customerApi).allowedOrigins(clientUrl);
                registry.addMapping(taskApi).allowedOrigins(clientUrl);
            }
        };
    }
}
