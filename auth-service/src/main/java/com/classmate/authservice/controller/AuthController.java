package com.classmate.authservice.controller;


import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.entity.Usuario;
import com.classmate.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(
            @Valid @RequestBody RegisterRequest request) {

        Usuario usuario = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
