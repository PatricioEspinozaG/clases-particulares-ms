package com.classmate.profesorservice;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.EstadoProfesor;
import com.classmate.profesorservice.entity.Profesor;
import com.classmate.profesorservice.repository.ProfesorRepository;
import com.classmate.profesorservice.service.ProfesorService;
import org.junit.jupiter.api.BeforeEach;
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

// @ExtendWith(MockitoExtension.class) → activa el framework Mockito para esta clase
@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    // @Mock → repositorio falso, no toca la base de datos real
    @Mock
    private ProfesorRepository profesorRepository;

    // @InjectMocks → crea ProfesorService real e inyecta el repositorio falso
    @InjectMocks
    private ProfesorService profesorService;

    // Datos reutilizables en todos los tests
    private Profesor profesorMock;
    private ProfesorRequest requestMock;

    // @BeforeEach → se ejecuta antes de cada test para preparar los datos
    @BeforeEach
    void setUp() {
        profesorMock = new Profesor(
                1L,
                100L,
                "Matemáticas",
                "Profesor con 5 años de experiencia",
                new BigDecimal("15000"),
                5,
                EstadoProfesor.ACTIVO
        );

        requestMock = new ProfesorRequest();
        requestMock.setUsuarioId(100L);
        requestMock.setEspecialidad("Matemáticas");
        requestMock.setDescripcion("Profesor con 5 años de experiencia");
        requestMock.setPrecioHora(new BigDecimal("15000"));
        requestMock.setExperienciaAnios(5);
        requestMock.setEstado(EstadoProfesor.ACTIVO);
    }

    // ─── TEST 1: Crear profesor exitoso ──────────────────────────────────────
    // GIVEN: el usuarioId no está registrado aún como profesor
    // WHEN:  llamamos a crear()
    // THEN:  retorna ProfesorResponse con los datos correctos
    @Test
    void crear_conDatosValidos_retornaProfesorResponse() {
        // GIVEN
        when(profesorRepository.findByUsuarioId(100L)).thenReturn(Optional.empty());
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesorMock);

        // WHEN
        ProfesorResponse response = profesorService.crear(requestMock);

        // THEN
        assertNotNull(response);
        assertEquals("Matemáticas", response.getEspecialidad());
        assertEquals(new BigDecimal("15000"), response.getPrecioHora());
        assertEquals(EstadoProfesor.ACTIVO, response.getEstado());

        // Verificamos que se llamó al repositorio exactamente 1 vez
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }

    // ─── TEST 2: Crear profesor con usuarioId duplicado lanza excepción ──────
    // GIVEN: ya existe un profesor con ese usuarioId
    // WHEN:  intentamos crear otro con el mismo usuarioId
    // THEN:  se lanza RuntimeException con el mensaje correcto
    @Test
    void crear_conUsuarioIdDuplicado_lanzaExcepcion() {
        // GIVEN
        when(profesorRepository.findByUsuarioId(100L)).thenReturn(Optional.of(profesorMock));

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profesorService.crear(requestMock));

        assertEquals("El usuario ya está registrado como profesor", ex.getMessage());

        // El repositorio NUNCA debió guardar nada
        verify(profesorRepository, never()).save(any());
    }

    // ─── TEST 3: Listar todos los profesores ─────────────────────────────────
    // GIVEN: hay 1 profesor en la BD
    // WHEN:  llamamos a listar()
    // THEN:  retorna lista con ese profesor
    @Test
    void listar_retornaListaDeProfesores() {
        // GIVEN
        when(profesorRepository.findAll()).thenReturn(List.of(profesorMock));

        // WHEN
        List<ProfesorResponse> resultado = profesorService.listar();

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getEspecialidad());
    }

    // ─── TEST 4: Buscar por ID existente ─────────────────────────────────────
    @Test
    void buscarPorId_conIdExistente_retornaProfesor() {
        // GIVEN
        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesorMock));

        // WHEN
        ProfesorResponse response = profesorService.buscarPorId(1L);

        // THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5, response.getExperienciaAnios());
    }

    // ─── TEST 5: Buscar por ID inexistente lanza excepción ───────────────────
    @Test
    void buscarPorId_conIdInexistente_lanzaExcepcion() {
        // GIVEN
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profesorService.buscarPorId(99L));

        assertEquals("Profesor no encontrado", ex.getMessage());
    }

    // ─── TEST 6: Buscar por usuarioId existente ───────────────────────────────
    @Test
    void buscarPorUsuarioId_conIdExistente_retornaProfesor() {
        // GIVEN
        when(profesorRepository.findByUsuarioId(100L)).thenReturn(Optional.of(profesorMock));

        // WHEN
        ProfesorResponse response = profesorService.buscarPorUsuarioId(100L);

        // THEN
        assertNotNull(response);
        assertEquals(100L, response.getUsuarioId());
    }

    // ─── TEST 7: Buscar por usuarioId inexistente lanza excepción ────────────
    @Test
    void buscarPorUsuarioId_conIdInexistente_lanzaExcepcion() {
        // GIVEN
        when(profesorRepository.findByUsuarioId(999L)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profesorService.buscarPorUsuarioId(999L));

        assertEquals("Profesor no encontrado", ex.getMessage());
    }

    // ─── TEST 8: Buscar por especialidad ─────────────────────────────────────
    // GIVEN: hay un profesor con especialidad "Matemáticas"
    // WHEN:  buscamos por "matem" (parcial, sin importar mayúsculas)
    // THEN:  retorna la lista con ese profesor
    @Test
    void buscarPorEspecialidad_retornaListaFiltrada() {
        // GIVEN
        when(profesorRepository.findByEspecialidadContainingIgnoreCase("matem"))
                .thenReturn(List.of(profesorMock));

        // WHEN
        List<ProfesorResponse> resultado = profesorService.buscarPorEspecialidad("matem");

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getEspecialidad());
    }

    // ─── TEST 9: Actualizar profesor existente ────────────────────────────────
    @Test
    void actualizar_conIdExistente_retornaProfesorActualizado() {
        // GIVEN
        Profesor actualizado = new Profesor(1L, 100L, "Física", "Actualizado",
                new BigDecimal("20000"), 8, EstadoProfesor.ACTIVO);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesorMock));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(actualizado);

        requestMock.setEspecialidad("Física");
        requestMock.setPrecioHora(new BigDecimal("20000"));

        // WHEN
        ProfesorResponse response = profesorService.actualizar(1L, requestMock);

        // THEN
        assertEquals("Física", response.getEspecialidad());
        assertEquals(new BigDecimal("20000"), response.getPrecioHora());
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }

    // ─── TEST 10: Eliminar profesor existente ────────────────────────────────
    @Test
    void eliminar_conIdExistente_eliminaCorrectamente() {
        // GIVEN
        when(profesorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(profesorRepository).deleteById(1L);

        // WHEN
        profesorService.eliminar(1L);

        // THEN: deleteById fue llamado exactamente 1 vez
        verify(profesorRepository, times(1)).deleteById(1L);
    }

    // ─── TEST 11: Eliminar profesor inexistente lanza excepción ──────────────
    @Test
    void eliminar_conIdInexistente_lanzaExcepcion() {
        // GIVEN
        when(profesorRepository.existsById(99L)).thenReturn(false);

        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profesorService.eliminar(99L));

        assertEquals("Profesor no encontrado", ex.getMessage());
        verify(profesorRepository, never()).deleteById(any());
    }
}
