package com.classmate.authservice.client;

import com.classmate.authservice.dto.UsuarioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "usuario-service")
public interface UsuarioClient {

    @PostMapping("/usuarios")
    void crearUsuario(@RequestBody UsuarioRequest request);
}