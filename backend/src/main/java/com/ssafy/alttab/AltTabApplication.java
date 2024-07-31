package com.ssafy.alttab;

import com.ssafy.alttab.common.config.UrlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.ssafy.alttab.security.redis.repository")
@EnableConfigurationProperties(UrlProperties.class)
@EnableJpaAuditing
public class AltTabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AltTabApplication.class, args);
    }
}
