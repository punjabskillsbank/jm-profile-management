package com.jobmatrix.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Job Matrix Profile Management API")
                        .version("1.0")
                        .description("API documentation for the Profile Management Microservice"))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://localhost:8082").description("Live")));
    }
}