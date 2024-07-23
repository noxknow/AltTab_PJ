package com.ssafy.alt_tab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@RequiredArgsConstructor
//@EnableWebSecurity
//@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login")
                            .permitAll() // 누구나 접근할 수 있도록 허용
                            .anyRequest() // 제외한 모든 요청을 대상으로 설정
                            .authenticated(); // 나머지 모든 요청이 인증된 사용자만 접근
                })
                .oauth2Login(Customizer.withDefaults()) // OAuth2 로그인 설정을 기본값으로 구성합니다. 이는 OAuth2 로그인에 필요한 기본적인 설정을 자동으로 적용합니다.
                .formLogin(Customizer.withDefaults()); // 폼 기반 로그인을 설정합니다. 기본값으로 설정되어, 로그인 폼과 관련된 기본적인 설정을 자동으로 적용합니다.
        return http.build();
    }
}
