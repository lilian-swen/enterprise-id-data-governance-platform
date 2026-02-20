package com.idgovern.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Configures how Spring handles web requests, static resources, and cross-origin traffic.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configure Cross-Origin Resource Sharing (CORS).
     * Essential if your frontend (like Vue or React) runs on a different port than 8082.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOriginPatterns("*") // Allow all origins (use specific domains for production)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache pre-flight response for 1 hour
    }

    /**
     * Configure Static Resource Handlers.
     * Maps URL paths to physical directories (like your JSP pages or CSS/JS files).
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /static/** to the resources/static/ folder
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // If you are using JSPs in /webapp/pages/, they are usually handled
        // by the ViewResolver in application.yml, but you can add custom mappings here.
    }
}