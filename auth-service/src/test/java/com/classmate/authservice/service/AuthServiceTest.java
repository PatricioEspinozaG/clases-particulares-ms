package com.classmate.authservice.service;

import com.classmate.authservice.client.UsuarioClient;
import com.classmate.authservice.dto.LoginRequest;
import com.classmate.authservice.dto.LoginResponse;
import com.classmate.authservice.dto.RegisterRequest;
import com.classmate.authservice.dto.RegisterResponse;
import com.classmate.authservice.entity.Role;
import com.classmate.authservice.entity.Usuario;
import com.classmate.authservice.exception.ResourceNotFoundException;
import com.classmate.authservice.repository.UsuarioRepository;
import com.classmate.authservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioClient usuarioClient;

    @Test
    public void testRegisterExitoso() {

        RegisterRequest request = new RegisterRequest();

        request.setNombre("Patricio");
        request.setApellido("Espinoza");
        request.setEmail("pato@test.cl");
        request.setPassword("123456");
        request.setTelefono("999999999");
        request.setFechaNacimiento(LocalDate.of(1995,1,1));

        when(usuarioRepository.findByEmail("pato@test.cl"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("passwordEncriptada");

        doNothing().when(usuarioClient)
                .crearUsuario(any());

        Usuario usuarioGuardado = new Usuario();

        usuarioGuardado.setId(1L);
        usuarioGuardado.setEmail("pato@test.cl");
        usuarioGuardado.setPassword("passwordEncriptada");
        usuarioGuardado.setRole(Role.ESTUDIANTE);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("pato@test.cl", response.getEmail());
        assertEquals(Role.ESTUDIANTE, response.getRole());

        verify(usuarioClient, times(1))
                .crearUsuario(any());
    }

    @Test
    public void testRegisterCorreoExistente() {

        RegisterRequest request = new RegisterRequest();

        request.setNombre("Patricio");
        request.setApellido("Espinoza");
        request.setEmail("pato@test.cl");
        request.setPassword("123456");
        request.setTelefono("999999999");
        request.setFechaNacimiento(LocalDate.of(1995,1,1));

        Usuario usuarioExistente = new Usuario();

        usuarioExistente.setId(1L);
        usuarioExistente.setEmail("pato@test.cl");

        when(usuarioRepository.findByEmail("pato@test.cl"))
                .thenReturn(Optional.of(usuarioExistente));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertEquals(
                "El correo ya existe",
                exception.getMessage()
        );

        verify(usuarioRepository, never())
                .save(any());
    }

    @Test
    public void testLoginExitoso() {

        LoginRequest request = new LoginRequest();

        request.setEmail("pato@test.cl");
        request.setPassword("123456");

        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setEmail("pato@test.cl");
        usuario.setPassword("passwordEncriptada");
        usuario.setRole(Role.ESTUDIANTE);

        when(usuarioRepository.findByEmail("pato@test.cl"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "123456",
                "passwordEncriptada"))
                .thenReturn(true);

        when(jwtService.generateToken("pato@test.cl"))
                .thenReturn("token-jwt");

        LoginResponse response =
                authService.login(request);

        assertNotNull(response);
        assertEquals("token-jwt",
                response.getToken());
    }

    @Test
    public void testLoginUsuarioNoEncontrado() {

        LoginRequest request = new LoginRequest();

        request.setEmail("inexistente@test.cl");
        request.setPassword("123456");

        when(usuarioRepository.findByEmail(
                "inexistente@test.cl"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> authService.login(request)
                );

        assertEquals(
                "Usuario no encontrado",
                exception.getMessage()
        );
    }

    @Test
    public void testLoginPasswordIncorrecta() {

        LoginRequest request = new LoginRequest();

        request.setEmail("pato@test.cl");
        request.setPassword("123456");

        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setEmail("pato@test.cl");
        usuario.setPassword("passwordEncriptada");

        when(usuarioRepository.findByEmail("pato@test.cl"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "123456",
                "passwordEncriptada"))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.login(request)
                );

        assertEquals(
                "Contraseña incorrecta",
                exception.getMessage()
        );
    }
}