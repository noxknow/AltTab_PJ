package com.ssafy.alttab.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.url")
@Getter
@Setter
public class UrlProperties {
    private String front;
    private String back;
}
