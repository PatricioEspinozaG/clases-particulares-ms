package com.classmate.claseservice.service;

import com.classmate.claseservice.client.ProfesorClient;
import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.entity.Clase;
import com.classmate.claseservice.exception.ResourceNotFoundException;
import com.classmate.claseservice.repository.ClaseRepository;
import com.classmate.claseservice.service.ClaseService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaseServiceTest {

    @Mock
    private ClaseRepository claseRepository;

    @Mock
    private ProfesorClient profesorClient;

    @InjectMocks
    private ClaseService claseService;

    private final Faker faker = new Faker();

    private Clase clase;
    private ClaseRequest request;

    @BeforeEach
    void setUp() {
        request = crearRequest();
        clase = crearClase(1L, request);
    }

    @Test
    void crearClaseDebeGuardarYRetornarResponse() {
        when(profesorClient.buscarPorId(request.getProfesorId())).thenReturn(new Object());
        when(claseRepository.save(any(Clase.class))).thenReturn(clase);

        ClaseResponse response = claseService.crearClase(request);

        assertNotNull(response);
        assertEquals(clase.getId(), response.getId());
        assertEquals(request.getAsignatura(), response.getAsignatura());
        assertEquals(request.getProfesorId(), response.getProfesorId());
        verify(profesorClient).buscarPorId(request.getProfesorId());
        verify(claseRepository).save(any(Clase.class));
    }

    @Test
    void obtenerClasesDebeRetornarLista() {
        when(claseRepository.findAll()).thenReturn(List.of(clase));

        List<ClaseResponse> response = claseService.obtenerClases();

        assertEquals(1, response.size());
        assertEquals(clase.getId(), response.get(0).getId());
        verify(claseRepository).findAll();
    }

    @Test
    void obtenerClasePorIdDebeRetornarClaseCuandoExiste() {
        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase));

        ClaseResponse response = claseService.obtenerClasePorId(1L);

        assertEquals(clase.getId(), response.getId());
        assertEquals(clase.getAsignatura(), response.getAsignatura());
        verify(claseRepository).findById(1L);
    }

    @Test
    void obtenerClasePorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(claseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> claseService.obtenerClasePorId(99L));

        verify(claseRepository).findById(99L);
    }

    @Test
    void actualizarClaseDebeActualizarYRetornarResponse() {
        when(profesorClient.buscarPorId(request.getProfesorId())).thenReturn(new Object());
        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase));
        when(claseRepository.save(any(Clase.class))).thenReturn(clase);

        ClaseResponse response = claseService.actualizarClase(1L, request);

        assertEquals(clase.getId(), response.getId());
        assertEquals(request.getAsignatura(), response.getAsignatura());
        verify(profesorClient).buscarPorId(request.getProfesorId());
        verify(claseRepository).save(any(Clase.class));
    }

    @Test
    void eliminarClaseDebeEliminarCuandoExiste() {
        when(claseRepository.existsById(1L)).thenReturn(true);

        claseService.eliminarClase(1L);

        verify(claseRepository).existsById(1L);
        verify(claseRepository).deleteById(1L);
    }

    @Test
    void eliminarClaseDebeLanzarExcepcionCuandoNoExiste() {
        when(claseRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> claseService.eliminarClase(99L));

        verify(claseRepository).existsById(99L);
        verify(claseRepository, never()).deleteById(99L);
    }

    private ClaseRequest crearRequest() {
        ClaseRequest request = new ClaseRequest();
        request.setAsignatura(faker.educator().course());
        request.setDescripcion(faker.lorem().sentence());
        request.setPrecio(faker.number().randomDouble(2, 5000, 50000));
        request.setFecha(LocalDateTime.now().plusDays(3));
        request.setDuracion(faker.number().numberBetween(30, 180));
        request.setProfesorId(faker.number().numberBetween(1L, 50L));
        return request;
    }

    private Clase crearClase(Long id, ClaseRequest request) {
        Clase clase = new Clase();
        clase.setId(id);
        clase.setAsignatura(request.getAsignatura());
        clase.setDescripcion(request.getDescripcion());
        clase.setPrecio(request.getPrecio());
        clase.setFecha(request.getFecha());
        clase.setDuracion(request.getDuracion());
        clase.setProfesorId(request.getProfesorId());
        return clase;
    }
}
