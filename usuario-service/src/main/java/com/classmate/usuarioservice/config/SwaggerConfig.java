package com.classmate.usuarioservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration → Spring registra esta clase al arrancar
// Define la metadata que aparece en la UI de Swagger
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuario Service API")
                        .version("1.0")
                        .description("Microservicio de gestión de usuarios del sistema ClassMate. " +
                                "Permite crear, listar, actualizar y eliminar usuarios.")
                );
    }
}
