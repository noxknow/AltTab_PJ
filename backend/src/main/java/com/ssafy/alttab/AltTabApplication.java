package com.ssafy.alttab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.ssafy.alttab.security.redis.repository")
@EnableJpaAuditing
public class AltTabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AltTabApplication.class, args);
    }
}
