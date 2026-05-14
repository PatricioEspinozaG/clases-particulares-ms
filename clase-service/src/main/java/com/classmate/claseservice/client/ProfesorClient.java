package com.classmate.claseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profesor-service",
        url = "http://localhost:8082"
)
public interface ProfesorClient {

    @GetMapping("/profesores/{id}")
    Object obtenerProfesorPorId(@PathVariable Long id);
}
