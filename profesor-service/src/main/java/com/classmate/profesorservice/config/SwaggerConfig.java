package com.classmate.profesorservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean // indica que un objeto será administrado por Spring
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Classmate - Profesor service")
                        .version("1.0")
                        .description("DOCUMENTACIÓN DE API SISTEMA DE PROFESOR"));
    }
}
