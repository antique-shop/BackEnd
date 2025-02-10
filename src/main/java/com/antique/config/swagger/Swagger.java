package com.antique.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth") // 모든 API에서 기본 인증 적용
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class Swagger {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info);
    }

    Info info = new Info().title("Antique").version("0.0.1").description(
            "프로젝트 [Antique]");
}
