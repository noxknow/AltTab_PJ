package com.ssafy.alttab;

import com.ssafy.alttab.config.UrlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.ssafy.alttab.security.redis.repository")
@EnableConfigurationProperties(UrlProperties.class)
public class AltTabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AltTabApplication.class, args);
    }
}
