package com.antique.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                 "/api/v1/login/**", "/api/v1/oauth2/callback",// 로그인 관련 API 허용
                                "/swagger-ui/**", "/v3/api-docs/**" // Swagger UI 및 OpenAPI 문서 허용
                        ).permitAll()
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .csrf(csrf -> csrf.disable()); // 개발 중에는 CSRF 보호 비활성화

        return http.build();
    }
}
