package com.santander.desafio.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("Desafio Santander API")
                .description("API do desafio: cadastro de agências e cálculo de distâncias")
                .version("1.0.0"));
    }
}
