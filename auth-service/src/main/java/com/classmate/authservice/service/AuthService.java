package com.classmate.authservice.service;

import com.classmate.authservice.dto.LoginRequest;
import com.classmate.authservice.dto.LoginResponse;
import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.dto.RegisterResponse;
import com.classmate.authservice.entity.Role;
import com.classmate.authservice.entity.TipoUsuario;
import com.classmate.authservice.entity.Usuario;
import com.classmate.authservice.exception.ResourceNotFoundException;
import com.classmate.authservice.repository.UsuarioRepository;
import com.classmate.authservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.classmate.authservice.client.UsuarioClient;
import com.classmate.authservice.dto.UsuarioRequest;

@Slf4j
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioClient usuarioClient;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, UsuarioClient usuarioClient) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usuarioClient = usuarioClient;
    }

    public RegisterResponse register(RegisterRequest request) {

        log.info("Registrando usuario con email {}",
                request.getEmail());

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {

            log.error("El correo {} ya existe",
                    request.getEmail());

            throw new RuntimeException("El correo ya existe");
        }

        Usuario usuario = new Usuario();

        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole(Role.ESTUDIANTE);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Usuario registrado correctamente con id {}",
                usuarioGuardado.getId());

        UsuarioRequest usuarioRequest = new UsuarioRequest();

        usuarioRequest.setAuthUserId(usuarioGuardado.getId());
        usuarioRequest.setNombre(request.getNombre());
        usuarioRequest.setApellido(request.getApellido());
        usuarioRequest.setEmail(request.getEmail());
        usuarioRequest.setTelefono(request.getTelefono());
        usuarioRequest.setFechaNacimiento(request.getFechaNacimiento());

        usuarioRequest.setTipoUsuario(TipoUsuario.ESTUDIANTE);

        usuarioClient.crearUsuario(usuarioRequest);

        log.info("Perfil usuario creado correctamente para authUserId {}",
                usuarioGuardado.getId());

        return new RegisterResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {

        log.info("Intento de login para email {}",
                request.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {

                    log.error("Usuario con email {} no encontrado",
                            request.getEmail());

                    return new ResourceNotFoundException(
                            "Usuario no encontrado");
                });

        boolean passwordCorrecta = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!passwordCorrecta) {

            log.error("Contraseña incorrecta para usuario {}",
                    request.getEmail());

            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario.getEmail());

        log.info("Login exitoso para usuario {}",
                usuario.getEmail());

        return new LoginResponse(token);
    }
}