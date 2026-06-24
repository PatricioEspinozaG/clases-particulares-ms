package com.classmate.profesorservice.service;

import com.classmate.profesorservice.client.UsuarioClient;
import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.EstadoProfesor;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.exception.ResourceNotFoundException;
import com.classmate.profesorservice.repository.ProfesorRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfesorServiceTest {

    @InjectMocks
    private ProfesorService profesorService;

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private UsuarioClient usuarioClient;

    // ─── TEST 1: Crear profesor exitoso ───────────────────────────────────────
    @Test
    public void testCrearProfesorExitoso() {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);
        request.setEspecialidad("Matemáticas");
        request.setDescripcion("Profesor con 5 años de experiencia");
        request.setPrecioHora(new BigDecimal("15000"));
        request.setExperienciaAnios(5);
        request.setEstado(EstadoProfesor.ACTIVO);

        // usuarioClient no lanza excepción → usuario existe
        when(usuarioClient.buscarPorId(100L))
                .thenReturn(new Object());

        when(profesorRepository.findByUsuarioId(100L))
                .thenReturn(Optional.empty());

        Profesor guardado = new Profesor(
                1L, 100L, "Matemáticas",
                "Profesor con 5 años de experiencia",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );
        when(profesorRepository.save(any(Profesor.class)))
                .thenReturn(guardado);

        ProfesorResponse response = profesorService.crear(request);

        assertNotNull(response);
        assertEquals("Matemáticas", response.getEspecialidad());
        assertEquals(EstadoProfesor.ACTIVO, response.getEstado());
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }

    // ─── TEST 2: Crear con usuarioId duplicado lanza excepción ────────────────
    @Test
    public void testCrearProfesorUsuarioIdDuplicado() {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);

        when(usuarioClient.buscarPorId(100L))
                .thenReturn(new Object());

        when(profesorRepository.findByUsuarioId(100L))
                .thenReturn(Optional.of(new Profesor()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profesorService.crear(request));

        assertEquals("El usuario ya está registrado como profesor", ex.getMessage());
        verify(profesorRepository, never()).save(any());
    }

    // ─── TEST 3: Crear con usuario inexistente (Feign 404) lanza excepción ────
    @Test
    public void testCrearProfesorUsuarioNoExiste() {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(999L);

        doThrow(mock(FeignException.NotFound.class))
                .when(usuarioClient).buscarPorId(999L);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> profesorService.crear(request)
        );

        assertTrue(ex.getMessage().contains("999"));
        verify(profesorRepository, never()).save(any());
    }

    // ─── TEST 4: Listar todos los profesores ──────────────────────────────────
    @Test
    public void testListarProfesores() {

        Profesor profesor = new Profesor(
                1L, 100L, "Matemáticas", "Descripción",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );

        when(profesorRepository.findAll()).thenReturn(List.of(profesor));

        List<ProfesorResponse> resultado = profesorService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getEspecialidad());
        verify(profesorRepository).findAll();
    }

    // ─── TEST 5: Buscar por ID existente ──────────────────────────────────────
    @Test
    public void testBuscarPorIdExistente() {

        Profesor profesor = new Profesor(
                1L, 100L, "Matemáticas", "Descripción",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));

        ProfesorResponse response = profesorService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5, response.getExperienciaAnios());
    }

    // ─── TEST 6: Buscar por ID inexistente lanza excepción ────────────────────
    @Test
    public void testBuscarPorIdNoExiste() {

        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> profesorService.buscarPorId(99L)
        );

        assertEquals("Profesor no encontrado", ex.getMessage());
    }

    // ─── TEST 7: Buscar por usuarioId existente ───────────────────────────────
    @Test
    public void testBuscarPorUsuarioId() {

        Profesor profesor = new Profesor(
                1L, 100L, "Matemáticas", "Descripción",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );

        when(profesorRepository.findByUsuarioId(100L))
                .thenReturn(Optional.of(profesor));

        ProfesorResponse response = profesorService.buscarPorUsuarioId(100L);

        assertNotNull(response);
        assertEquals(100L, response.getUsuarioId());
    }

    // ─── TEST 8: Buscar por especialidad ──────────────────────────────────────
    @Test
    public void testBuscarPorEspecialidad() {

        Profesor profesor = new Profesor(
                1L, 100L, "Matemáticas", "Descripción",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );

        when(profesorRepository.findByEspecialidadContainingIgnoreCase("matem"))
                .thenReturn(List.of(profesor));

        List<ProfesorResponse> resultado =
                profesorService.buscarPorEspecialidad("matem");

        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getEspecialidad());
    }

    // ─── TEST 9: Eliminar profesor existente ──────────────────────────────────
    @Test
    public void testEliminarProfesor() {

        when(profesorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(profesorRepository).deleteById(1L);

        profesorService.eliminar(1L);

        verify(profesorRepository, times(1)).deleteById(1L);
    }

    // ─── TEST 10: Eliminar profesor inexistente lanza excepción ───────────────
    @Test
    public void testEliminarProfesorNoExiste() {

        when(profesorRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> profesorService.eliminar(99L)
        );

        assertEquals("Profesor no encontrado", ex.getMessage());
        verify(profesorRepository, never()).deleteById(any());
    }

    @Test
    public void testActualizarProfesorExitoso() {

        Long id = 1L;

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);
        request.setEspecialidad("Física");
        request.setDescripcion("Actualizado");
        request.setPrecioHora(new BigDecimal("20000"));
        request.setExperienciaAnios(10);
        request.setEstado(EstadoProfesor.ACTIVO);

        // Mock Feign (usuario existe)
        when(usuarioClient.buscarPorId(100L))
                .thenReturn(new Object());

        Profesor profesorExistente = new Profesor(
                1L, 100L, "Matemáticas", "Antiguo",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );

        when(profesorRepository.findById(id))
                .thenReturn(Optional.of(profesorExistente));

        when(profesorRepository.save(any(Profesor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProfesorResponse response = profesorService.actualizar(id, request);

        assertNotNull(response);
        assertEquals("Física", response.getEspecialidad());
        assertEquals("Actualizado", response.getDescripcion());
        assertEquals(10, response.getExperienciaAnios());

        verify(profesorRepository).save(any(Profesor.class));
    }

    @Test
    public void testActualizarProfesorUsuarioNoExiste() {

        Long id = 1L;

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(999L);
        request.setEspecialidad("Física");
        request.setDescripcion("Actualizado");
        request.setPrecioHora(new BigDecimal("20000"));
        request.setExperienciaAnios(10);
        request.setEstado(EstadoProfesor.ACTIVO);

        doThrow(mock(FeignException.NotFound.class))
                .when(usuarioClient).buscarPorId(999L);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> profesorService.actualizar(id, request)
        );

        assertTrue(ex.getMessage().contains("999"));

        verify(profesorRepository, never()).save(any());
    }

    @Test
    public void testActualizarProfesorNoExiste() {

        Long id = 99L;

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);
        request.setEspecialidad("Física");
        request.setDescripcion("Actualizado");
        request.setPrecioHora(new BigDecimal("20000"));
        request.setExperienciaAnios(10);
        request.setEstado(EstadoProfesor.ACTIVO);

        when(usuarioClient.buscarPorId(100L))
                .thenReturn(new Object());

        when(profesorRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> profesorService.actualizar(id, request)
        );

        assertEquals("Profesor no encontrado", ex.getMessage());

        verify(profesorRepository, never()).save(any());
    }

    @Test
    public void testCrearProfesorErrorConexionUsuarioService() {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);

        doThrow(mock(FeignException.class))
                .when(usuarioClient).buscarPorId(100L);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> profesorService.crear(request)
        );

        assertEquals("No se puede conectar con usuario-service", ex.getMessage());

        verify(profesorRepository, never()).save(any());
    }

    @Test
    public void testEliminarProfesorExitoso() {

        when(profesorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(profesorRepository).deleteById(1L);

        profesorService.eliminar(1L);

        verify(profesorRepository).deleteById(1L);
    }

    @Test
    public void testListarProfesoresVacio() {

        when(profesorRepository.findAll()).thenReturn(List.of());

        List<ProfesorResponse> resultado = profesorService.listar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorEspecialidadSinResultados() {

        when(profesorRepository.findByEspecialidadContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<ProfesorResponse> resultado =
                profesorService.buscarPorEspecialidad("xyz");

        assertTrue(resultado.isEmpty());
    }

}
