package com.classmate.profesorservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service")
public interface UsuarioClient {

    @GetMapping("/usuarios/{id}")
    Object buscarPorId(@PathVariable Long id);
}