package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagoService pagoService;

    private final Faker faker = new Faker();
    private PagoRequest request;
    private PagoResponse response;

    @BeforeEach
    void setUp() {
        request = new PagoRequest();
        request.setReservaId(faker.number().numberBetween(1L, 100L));
        request.setMonto(faker.number().randomDouble(2, 10000, 90000));
        request.setMetodoPago("TARJETA");

        response = new PagoResponse(
                1L,
                request.getReservaId(),
                request.getMonto(),
                request.getMetodoPago(),
                EstadoPago.PENDIENTE,
                LocalDateTime.now()
        );
    }

    @Test
    void crearPagoDeberiaRetornarCreatedConLinks() throws Exception {
        when(pagoService.crearPago(any(PagoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/pagos")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.aprobar.href").exists())
                .andExpect(jsonPath("$._links.rechazar.href").exists());
    }

    @Test
    void crearPagoDeberiaRetornarBadRequestCuandoMontoEsNegativo() throws Exception {
        request.setMonto(-1000.0);

        mockMvc.perform(post("/pagos")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerPagosDeberiaRetornarColeccionConLinks() throws Exception {
        when(pagoService.obtenerPagos()).thenReturn(List.of(response));

        mockMvc.perform(get("/pagos")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagoResponseList", hasSize(1)))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void obtenerPagoPorIdDeberiaRetornarPagoConLinks() throws Exception {
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/pagos/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void aprobarPago_DeberiaRetornarPagoAprobado() throws Exception {
        PagoResponse aprobado = new PagoResponse(
                1L,
                response.getReservaId(),
                response.getMonto(),
                response.getMetodoPago(),
                EstadoPago.APROBADO,
                response.getFechaPago()
        );

        when(pagoService.aprobarPago(1L)).thenReturn(aprobado);

        mockMvc.perform(put("/pagos/1/aprobar")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void rechazarPagoDeberiaRetornarPagoRechazado() throws Exception {
        PagoResponse rechazado = new PagoResponse(
                1L,
                response.getReservaId(),
                response.getMonto(),
                response.getMetodoPago(),
                EstadoPago.RECHAZADO,
                response.getFechaPago()
        );

        when(pagoService.rechazarPago(1L)).thenReturn(rechazado);

        mockMvc.perform(put("/pagos/1/rechazar")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RECHAZADO"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void buscarPorEstadoDeberiaRetornarColeccionFiltrada() throws Exception {
        when(pagoService.buscarPorEstado(EstadoPago.PENDIENTE)).thenReturn(List.of(response));

        mockMvc.perform(get("/pagos/estado/PENDIENTE")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagoResponseList", hasSize(1)))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void eliminarPagoDeberiaRetornarNoContent() throws Exception {
        doNothing().when(pagoService).eliminarPago(1L);

        mockMvc.perform(delete("/pagos/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}