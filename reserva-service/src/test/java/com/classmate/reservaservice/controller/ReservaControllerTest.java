package com.classmate.reservaservice.controller;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
import com.classmate.reservaservice.entity.EstadoReserva;
import com.classmate.reservaservice.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservaResponse reservaResponse;

    @BeforeEach
    void setUp() {

        reservaResponse = new ReservaResponse(
                1L,
                1L,
                2L,
                3L,
                LocalDateTime.now().plusDays(1),
                EstadoReserva.PENDIENTE
        );
    }

    @Test
    public void testCrearReserva() throws Exception {

        CreateReservaRequest request =
                new CreateReservaRequest();

        request.setUsuarioId(1L);
        request.setProfesorId(2L);
        request.setClaseId(3L);
        request.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );

        when(reservaService.crearReserva(any()))
                .thenReturn(reservaResponse);

        mockMvc.perform(
                        post("/reservas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testObtenerReservas() throws Exception {

        when(reservaService.obtenerReservas())
                .thenReturn(List.of(reservaResponse));

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void testObtenerReservaPorId() throws Exception {

        when(reservaService.obtenerReservaPorId(1L))
                .thenReturn(reservaResponse);

        mockMvc.perform(get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCancelarReserva() throws Exception {

        ReservaResponse cancelada =
                new ReservaResponse(
                        1L,
                        1L,
                        2L,
                        3L,
                        LocalDateTime.now(),
                        EstadoReserva.CANCELADA
                );

        when(reservaService.cancelarReserva(1L))
                .thenReturn(cancelada);

        mockMvc.perform(put("/reservas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado")
                        .value("CANCELADA"));
    }

    @Test
    public void testConfirmarReserva() throws Exception {

        ReservaResponse confirmada =
                new ReservaResponse(
                        1L,
                        1L,
                        2L,
                        3L,
                        LocalDateTime.now(),
                        EstadoReserva.CONFIRMADA
                );

        when(reservaService.confirmarReserva(1L))
                .thenReturn(confirmada);

        mockMvc.perform(put("/reservas/1/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado")
                        .value("CONFIRMADA"));
    }
}