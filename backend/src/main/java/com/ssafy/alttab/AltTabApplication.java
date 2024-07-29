package com.ssafy.alttab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.ssafy.alttab.redis.repository")
public class AltTabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AltTabApplication.class, args);
    }
}
