package com.hms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenAPI Documentation for a Hotel Management System.",
                title = "OpenAPI Specification",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        },
        tags = {

        }
)
@SecurityScheme(
        name = "bearAuth",
        type = SecuritySchemeType.HTTP
)
@Configuration
public class OpenApiConfig{
        @Bean
        public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .openapi("3.1.0");
        }
}