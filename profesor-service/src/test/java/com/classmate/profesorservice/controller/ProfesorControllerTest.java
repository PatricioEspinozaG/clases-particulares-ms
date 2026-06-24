package com.classmate.profesorservice.controller;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.entity.EstadoProfesor;
import com.classmate.profesorservice.service.ProfesorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfesorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProfesorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesorService profesorService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfesorResponse profesorResponse;

    @BeforeEach
    void setUp() {
        profesorResponse = new ProfesorResponse(
                1L, 100L, "Matemáticas",
                "Profesor con 5 años de experiencia",
                new BigDecimal("15000"), 5, EstadoProfesor.ACTIVO
        );
    }

    // ─── TEST 1: POST /profesores — crear profesor exitoso ───────────────────
    @Test
    public void testCrearProfesor() throws Exception {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);
        request.setEspecialidad("Matemáticas");
        request.setDescripcion("Profesor con 5 años de experiencia");
        request.setPrecioHora(new BigDecimal("15000"));
        request.setExperienciaAnios(5);
        request.setEstado(EstadoProfesor.ACTIVO);

        when(profesorService.crear(any())).thenReturn(profesorResponse);

        mockMvc.perform(
                        post("/profesores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.especialidad").value("Matemáticas"))
                .andExpect(jsonPath("$.precioHora").value(15000));
    }

    // ─── TEST 2: GET /profesores — listar todos ───────────────────────────────
    @Test
    public void testListarProfesores() throws Exception {

        when(profesorService.listar()).thenReturn(List.of(profesorResponse));

        mockMvc.perform(get("/profesores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].especialidad").value("Matemáticas"));
    }

    // ─── TEST 3: GET /profesores/{id} — buscar por ID ────────────────────────
    @Test
    public void testBuscarPorId() throws Exception {

        when(profesorService.buscarPorId(1L)).thenReturn(profesorResponse);

        mockMvc.perform(get("/profesores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.experienciaAnios").value(5));
    }

    // ─── TEST 4: GET /profesores/usuario/{usuarioId} ──────────────────────────
    @Test
    public void testBuscarPorUsuarioId() throws Exception {

        when(profesorService.buscarPorUsuarioId(100L)).thenReturn(profesorResponse);

        mockMvc.perform(get("/profesores/usuario/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(100));
    }

    // ─── TEST 5: GET /profesores/especialidad/{especialidad} ──────────────────
    @Test
    public void testBuscarPorEspecialidad() throws Exception {

        when(profesorService.buscarPorEspecialidad("Matemáticas"))
                .thenReturn(List.of(profesorResponse));

        mockMvc.perform(get("/profesores/especialidad/Matemáticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especialidad").value("Matemáticas"));
    }

    // ─── TEST 6: PUT /profesores/{id} — actualizar ────────────────────────────
    @Test
    public void testActualizarProfesor() throws Exception {

        ProfesorRequest request = new ProfesorRequest();
        request.setUsuarioId(100L);
        request.setEspecialidad("Física");
        request.setDescripcion("Actualizado");
        request.setPrecioHora(new BigDecimal("20000"));
        request.setExperienciaAnios(8);
        request.setEstado(EstadoProfesor.ACTIVO);

        when(profesorService.actualizar(eq(1L), any())).thenReturn(profesorResponse);

        mockMvc.perform(
                        put("/profesores/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ─── TEST 7: DELETE /profesores/{id} — eliminar ───────────────────────────
    @Test
    public void testEliminarProfesor() throws Exception {

        doNothing().when(profesorService).eliminar(1L);

        mockMvc.perform(delete("/profesores/1"))
                .andExpect(status().isNoContent());

        verify(profesorService, times(1)).eliminar(1L);
    }
}
