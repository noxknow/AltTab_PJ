package com.ssafy.alt_tab.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMVCConfig implements WebMvcConfigurer {

//    private final String DEVELOP_FRONT_ADDRESS = "http://70.12.246.179/:3000";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins(DEVELOP_FRONT_ADDRESS)
//                .allowCredentials(true)
//                .allowedOrigins("*")
//                .allowCredentials(false)
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .exposedHeaders("location")
//                .allowedHeaders("*");
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
