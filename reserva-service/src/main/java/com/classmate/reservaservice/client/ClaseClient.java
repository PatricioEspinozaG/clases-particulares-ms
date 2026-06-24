package com.classmate.reservaservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clase-service")
public interface ClaseClient {

    @GetMapping("/clases/{id}")
    Object obtenerClasePorId(
            @PathVariable Long id);
}