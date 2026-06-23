package com.classmate.usuarioservice;

import com.classmate.usuarioservice.dto.UsuarioRequest;
import com.classmate.usuarioservice.dto.UsuarioResponse;
import com.classmate.usuarioservice.entity.TipoUsuario;
import com.classmate.usuarioservice.entity.Usuario;
import com.classmate.usuarioservice.repository.UsuarioRepository;
import com.classmate.usuarioservice.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
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

// @ExtendWith(MockitoExtension.class) → activa Mockito en esta clase de test
// Sin esto, los @Mock y @InjectMocks no funcionan
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // @Mock → crea un "doble falso" del repositorio
    // No toca la base de datos real, solo simula respuestas
    @Mock
    private UsuarioRepository usuarioRepository;

    // @InjectMocks → crea el servicio real e inyecta el @Mock anterior
    // Es decir: UsuarioService usará el repositorio falso
    @InjectMocks
    private UsuarioService usuarioService;

    // Variables reutilizables en todos los tests
    private Usuario usuarioMock;
    private UsuarioRequest requestMock;

    // @BeforeEach → se ejecuta ANTES de cada test
    // Aquí preparamos los datos de prueba comunes
    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario(
                1L,
                10L,
                "Juan",
                "Pérez",
                "juan@mail.com",
                "+56912345678",
                LocalDate.of(1995, 5, 20),
                TipoUsuario.ESTUDIANTE
        );

        requestMock = new UsuarioRequest();
        requestMock.setAuthUserId(10L);
        requestMock.setNombre("Juan");
        requestMock.setApellido("Pérez");
        requestMock.setEmail("juan@mail.com");
        requestMock.setTelefono("+56912345678");
        requestMock.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        requestMock.setTipoUsuario(TipoUsuario.ESTUDIANTE);
    }

    // ─── TEST 1: Crear usuario exitoso ───────────────────────────────────────
    // GIVEN: no existe el email ni el authUserId en la BD
    // WHEN:  llamamos a crear()
    // THEN:  devuelve un UsuarioResponse con los datos correctos
    @Test
    void crear_conDatosValidos_retornaUsuarioResponse() {
        // GIVEN
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByAuthUserId(10L)).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // WHEN
        UsuarioResponse response = usuarioService.crear(requestMock);

        // THEN
        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@mail.com", response.getEmail());
        assertEquals(TipoUsuario.ESTUDIANTE, response.getTipoUsuario());

        // Verificamos que el repositorio fue llamado exactamente 1 vez
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ─── TEST 2: Crear usuario con email duplicado lanza excepción ────────────
    // GIVEN: ya existe un usuario con ese email
    // WHEN:  intentamos crear otro con el mismo email
    // THEN:  se lanza RuntimeException con el mensaje correcto
    @Test
    void crear_conEmailDuplicado_lanzaExcepcion() {
        // GIVEN
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(usuarioMock));

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crear(requestMock));

        assertEquals("El correo ya existe", ex.getMessage());

        // El repositorio NUNCA debió guardar nada
        verify(usuarioRepository, never()).save(any());
    }

    // ─── TEST 3: Crear con authUserId duplicado lanza excepción ──────────────
    @Test
    void crear_conAuthUserIdDuplicado_lanzaExcepcion() {
        // GIVEN: email libre, pero authUserId ya existe
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByAuthUserId(10L)).thenReturn(Optional.of(usuarioMock));

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.crear(requestMock));

        assertEquals("El authUserId ya está asociado a un usuario", ex.getMessage());
    }

    // ─── TEST 4: Listar todos los usuarios ────────────────────────────────────
    // GIVEN: hay 1 usuario en la BD
    // WHEN:  llamamos a listar()
    // THEN:  retorna una lista con ese usuario
    @Test
    void listar_retornaListaDeUsuarios() {
        // GIVEN
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioMock));

        // WHEN
        List<UsuarioResponse> resultado = usuarioService.listar();

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    // ─── TEST 5: Buscar por ID existente ──────────────────────────────────────
    @Test
    void buscarPorId_conIdExistente_retornaUsuario() {
        // GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // WHEN
        UsuarioResponse response = usuarioService.buscarPorId(1L);

        // THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Pérez", response.getApellido());
    }

    // ─── TEST 6: Buscar por ID inexistente lanza excepción ───────────────────
    @Test
    void buscarPorId_conIdInexistente_lanzaExcepcion() {
        // GIVEN
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorId(99L));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // ─── TEST 7: Actualizar usuario existente ────────────────────────────────
    @Test
    void actualizar_conIdExistente_retornaUsuarioActualizado() {
        // GIVEN
        Usuario actualizado = new Usuario(1L, 10L, "Juan Modificado", "Pérez",
                "juan@mail.com", "+56999999999",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);

        requestMock.setNombre("Juan Modificado");

        // WHEN
        UsuarioResponse response = usuarioService.actualizar(1L, requestMock);

        // THEN
        assertEquals("Juan Modificado", response.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ─── TEST 8: Eliminar usuario existente ──────────────────────────────────
    @Test
    void eliminar_conIdExistente_eliminaCorrectamente() {
        // GIVEN
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // WHEN
        usuarioService.eliminar(1L);

        // THEN: verificamos que deleteById fue llamado 1 vez con ID 1
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // ─── TEST 9: Eliminar usuario inexistente lanza excepción ────────────────
    @Test
    void eliminar_conIdInexistente_lanzaExcepcion() {
        // GIVEN
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.eliminar(99L));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(any());
    }

    // ─── TEST 10: Buscar por authUserId existente ────────────────────────────
    @Test
    void buscarPorAuthUserId_conIdExistente_retornaUsuario() {
        // GIVEN
        when(usuarioRepository.findByAuthUserId(10L)).thenReturn(Optional.of(usuarioMock));

        // WHEN
        UsuarioResponse response = usuarioService.buscarPorAuthUserId(10L);

        // THEN
        assertNotNull(response);
        assertEquals(10L, response.getAuthUserId());
    }
}
