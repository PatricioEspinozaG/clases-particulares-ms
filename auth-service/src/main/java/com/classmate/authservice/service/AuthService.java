package com.classmate.authservice.service;

import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.entity.Role;
import com.classmate.authservice.entity.Usuario;
import com.classmate.authservice.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario register(RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario usuario = new Usuario();

        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole(Role.ESTUDIANTE);

        return usuarioRepository.save(usuario);
    }
}
