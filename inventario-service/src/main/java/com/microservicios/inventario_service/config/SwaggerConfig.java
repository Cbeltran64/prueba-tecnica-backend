package com.microservicios.inventario_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Inventario Service")
                        .version("1.0")
                        .description("Documentación de endpoints del microservicio de inventario"));
    }
}
