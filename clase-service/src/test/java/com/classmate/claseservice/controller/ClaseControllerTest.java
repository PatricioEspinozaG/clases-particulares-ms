package com.classmate.claseservice.controller;

import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.service.ClaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaseControllerTest {

    @Mock
    private ClaseService claseService;

    private ClaseController claseController;
    private final Faker faker = new Faker();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private ClaseRequest request;
    private ClaseResponse response;

    @BeforeEach
    void setUp() {
        claseController = new ClaseController(claseService);
        request = crearRequest();
        response = crearResponse(1L, request);
    }

    @Test
    void crearClaseDebeRetornarCreatedConLinks() {
        when(claseService.crearClase(request)).thenReturn(response);

        ResponseEntity<EntityModel<ClaseResponse>> result = claseController.crearClase(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response.getId(), Objects.requireNonNull(result.getBody()).getContent().getId());
        assertTrue(result.getBody().hasLink("self"));
        assertTrue(result.getBody().hasLink("clases"));
        assertTrue(result.getBody().hasLink("eliminar"));
        verify(claseService).crearClase(request);
    }

    @Test
    void obtenerClasePorIdDebeRetornarOkConLinks() {
        when(claseService.obtenerClasePorId(1L)).thenReturn(response);

        ResponseEntity<EntityModel<ClaseResponse>> result = claseController.obtenerClasePorId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response.getId(), Objects.requireNonNull(result.getBody()).getContent().getId());
        assertTrue(result.getBody().hasLink("self"));
        verify(claseService).obtenerClasePorId(1L);
    }

    @Test
    void eliminarClaseDebeRetornarMensajeCorrecto() {
        doNothing().when(claseService).eliminarClase(1L);

        ResponseEntity<String> result = claseController.eliminarClase(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Clase eliminada correctamente", result.getBody());
        verify(claseService).eliminarClase(1L);
    }

    @Test
    void requestDebeSerializarFechaCorrectamente() throws Exception {
        String json = objectMapper.writeValueAsString(request);

        assertTrue(json.contains("asignatura"));
        assertTrue(json.contains("profesorId"));
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

    private ClaseResponse crearResponse(Long id, ClaseRequest request) {
        return new ClaseResponse(
                id,
                request.getAsignatura(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getFecha(),
                request.getDuracion(),
                request.getProfesorId()
        );
    }
}
