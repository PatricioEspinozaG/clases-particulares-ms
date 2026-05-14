package com.classmate.authservice.service;

import com.classmate.authservice.dto.LoginRequest;
import com.classmate.authservice.dto.LoginResponse;
import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.dto.RegisterResponse;
import com.classmate.authservice.entity.Role;
import com.classmate.authservice.entity.Usuario;
import com.classmate.authservice.repository.UsuarioRepository;
import com.classmate.authservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario usuario = new Usuario();

        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole(Role.ESTUDIANTE);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new RegisterResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        boolean passwordCorrecta = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!passwordCorrecta) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario.getEmail());

        return new LoginResponse(token);
    }
}