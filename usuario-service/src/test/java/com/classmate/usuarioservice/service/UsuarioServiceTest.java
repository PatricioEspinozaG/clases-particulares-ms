package com.classmate.usuarioservice.service;

import com.classmate.usuarioservice.dto.UsuarioRequest;
import com.classmate.usuarioservice.dto.UsuarioResponse;
import com.classmate.usuarioservice.entity.TipoUsuario;
import com.classmate.usuarioservice.entity.Usuario;
import com.classmate.usuarioservice.exception.ResourceNotFoundException;
import com.classmate.usuarioservice.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    // ─── TEST 1: Crear usuario exitoso ────────────────────────────────────────
    @Test
    public void testCrearUsuarioExitoso() {

        UsuarioRequest request = new UsuarioRequest();
        request.setAuthUserId(10L);
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@mail.com");
        request.setTelefono("+56912345678");
        request.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        request.setTipoUsuario(TipoUsuario.ESTUDIANTE);

        when(usuarioRepository.findByEmail("juan@mail.com"))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findByAuthUserId(10L))
                .thenReturn(Optional.empty());

        Usuario guardado = new Usuario(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );
        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(guardado);

        UsuarioResponse response = usuarioService.crear(request);

        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@mail.com", response.getEmail());
        assertEquals(TipoUsuario.ESTUDIANTE, response.getTipoUsuario());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ─── TEST 2: Crear con email duplicado lanza excepción ────────────────────
    @Test
    public void testCrearUsuarioEmailDuplicado() {

        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("juan@mail.com");
        request.setAuthUserId(10L);

        when(usuarioRepository.findByEmail("juan@mail.com"))
                .thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crear(request));

        assertEquals("El correo ya existe", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    // ─── TEST 3: Crear con authUserId duplicado lanza excepción ───────────────
    @Test
    public void testCrearUsuarioAuthUserIdDuplicado() {

        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("nuevo@mail.com");
        request.setAuthUserId(10L);

        when(usuarioRepository.findByEmail("nuevo@mail.com"))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findByAuthUserId(10L))
                .thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crear(request));

        assertEquals("El authUserId ya está asociado a un usuario", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    // ─── TEST 4: Listar todos los usuarios ────────────────────────────────────
    @Test
    public void testListarUsuarios() {

        Usuario usuario = new Usuario(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> resultado = usuarioService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(usuarioRepository).findAll();
    }

    // ─── TEST 5: Buscar por ID existente ──────────────────────────────────────
    @Test
    public void testBuscarPorIdExistente() {

        Usuario usuario = new Usuario(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponse response = usuarioService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Pérez", response.getApellido());
    }

    // ─── TEST 6: Buscar por ID inexistente lanza excepción ────────────────────
    @Test
    public void testBuscarPorIdNoExiste() {

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // ─── TEST 7: Buscar por authUserId existente ──────────────────────────────
    @Test
    public void testBuscarPorAuthUserId() {

        Usuario usuario = new Usuario(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );

        when(usuarioRepository.findByAuthUserId(10L))
                .thenReturn(Optional.of(usuario));

        UsuarioResponse response = usuarioService.buscarPorAuthUserId(10L);

        assertNotNull(response);
        assertEquals(10L, response.getAuthUserId());
    }

    // ─── TEST 8: Actualizar usuario existente ─────────────────────────────────
    @Test
    public void testActualizarUsuario() {

        Usuario existente = new Usuario(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );

        UsuarioRequest request = new UsuarioRequest();
        request.setAuthUserId(10L);
        request.setNombre("Juan Actualizado");
        request.setApellido("Pérez");
        request.setEmail("juan@mail.com");
        request.setTelefono("+56999999999");
        request.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        request.setTipoUsuario(TipoUsuario.ESTUDIANTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse response = usuarioService.actualizar(1L, request);

        assertEquals("Juan Actualizado", response.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ─── TEST 9: Eliminar usuario existente ───────────────────────────────────
    @Test
    public void testEliminarUsuario() {

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // ─── TEST 10: Eliminar usuario inexistente lanza excepción ────────────────
    @Test
    public void testEliminarUsuarioNoExiste() {

        when(usuarioRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.eliminar(99L)
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(any());
    }
}
