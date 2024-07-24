package com.ssafy.alt_tab.config;

import com.ssafy.alt_tab.jwt.JWTUtil;
import com.ssafy.alt_tab.member.service.MemberOAuth2Service;
import com.ssafy.alt_tab.oauth2.SuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final MemberOAuth2Service memberOAuth2Service;
    private final SuccessHandler successHandler;
    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> { auth
//                        .requestMatchers("/","/login")
//                            .permitAll() // 누구나 접근할 수 있도록 허용
////                        .requestMatchers("/admin").hanRole("ADMIN")
////                        .requestMatchers("/my/**").hanAnyRole("ADMIN","USER")
//                        .anyRequest().authenticated();
//                })
//                .oauth2Login(Customizer.withDefaults()) // OAuth2 로그인 설정을 기본값으로 구성합니다. 이는 OAuth2 로그인에 필요한 기본적인 설정을 자동으로 적용합니다.
//                .formLogin(Customizer.withDefaults()); // 폼 기반 로그인을 설정합니다. 기본값으로 설정되어, 로그인 폼과 관련된 기본적인 설정을 자동으로 적용합니다.
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));


        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(memberOAuth2Service))
                        .successHandler(successHandler)
                );

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
