package com.antique.config.Swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info);
    }

    Info info = new Info().title("Antique").version("0.0.1").description(
            "프로젝트 [Antique]");
}

