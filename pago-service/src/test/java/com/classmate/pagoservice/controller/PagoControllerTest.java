package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.service.PagoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    @Mock
    private PagoService pagoService;

    private PagoController pagoController;
    private Faker faker;
    private PagoRequest request;
    private PagoResponse response;

    @BeforeEach
    void setUp() {
        pagoController = new PagoController(pagoService);
        faker = new Faker();

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
    void crearPagoDebeRetornarCreatedConLinks() {
        when(pagoService.crearPago(any(PagoRequest.class))).thenReturn(response);

        ResponseEntity<EntityModel<PagoResponse>> resultado = pagoController.crearPago(request);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).isEqualTo(response);
        assertThat(resultado.getBody().getLink("self")).isPresent();
        assertThat(resultado.getBody().getLink("pagos")).isPresent();
        assertThat(resultado.getBody().getLink("buscar-por-estado")).isPresent();
        assertThat(resultado.getBody().getLink("aprobar")).isPresent();
        assertThat(resultado.getBody().getLink("rechazar")).isPresent();

        verify(pagoService).crearPago(any(PagoRequest.class));
    }

    @Test
    void obtenerPagosDebeRetornarCollectionModelConLinks() {
        when(pagoService.obtenerPagos()).thenReturn(List.of(response));

        ResponseEntity<CollectionModel<EntityModel<PagoResponse>>> resultado = pagoController.obtenerPagos();

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).hasSize(1);
        assertThat(resultado.getBody().getLink("self")).isPresent();

        verify(pagoService).obtenerPagos();
    }

    @Test
    void obtenerPagoPorIdDebeRetornarPagoConLinks() {
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(response);

        ResponseEntity<EntityModel<PagoResponse>> resultado = pagoController.obtenerPagoPorId(1L);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).isEqualTo(response);
        assertThat(resultado.getBody().getLink("self")).isPresent();
        assertThat(resultado.getBody().getLink("pagos")).isPresent();

        verify(pagoService).obtenerPagoPorId(1L);
    }

    @Test
    void aprobarPagoDebeRetornarPagoAprobadoConLinks() {
        PagoResponse aprobado = new PagoResponse(
                1L,
                response.getReservaId(),
                response.getMonto(),
                response.getMetodoPago(),
                EstadoPago.APROBADO,
                response.getFechaPago()
        );

        when(pagoService.aprobarPago(1L)).thenReturn(aprobado);

        ResponseEntity<EntityModel<PagoResponse>> resultado = pagoController.aprobarPago(1L);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).isEqualTo(aprobado);
        assertThat(resultado.getBody().getContent().getEstado()).isEqualTo(EstadoPago.APROBADO);
        assertThat(resultado.getBody().getLink("self")).isPresent();
        assertThat(resultado.getBody().getLink("pagos")).isPresent();
        assertThat(resultado.getBody().getLink("aprobar")).isNotPresent();
        assertThat(resultado.getBody().getLink("rechazar")).isNotPresent();

        verify(pagoService).aprobarPago(1L);
    }

    @Test
    void rechazarPagoDebeRetornarPagoRechazadoConLinks() {
        PagoResponse rechazado = new PagoResponse(
                1L,
                response.getReservaId(),
                response.getMonto(),
                response.getMetodoPago(),
                EstadoPago.RECHAZADO,
                response.getFechaPago()
        );

        when(pagoService.rechazarPago(1L)).thenReturn(rechazado);

        ResponseEntity<EntityModel<PagoResponse>> resultado = pagoController.rechazarPago(1L);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).isEqualTo(rechazado);
        assertThat(resultado.getBody().getContent().getEstado()).isEqualTo(EstadoPago.RECHAZADO);
        assertThat(resultado.getBody().getLink("self")).isPresent();
        assertThat(resultado.getBody().getLink("pagos")).isPresent();
        assertThat(resultado.getBody().getLink("aprobar")).isNotPresent();
        assertThat(resultado.getBody().getLink("rechazar")).isNotPresent();

        verify(pagoService).rechazarPago(1L);
    }

    @Test
    void buscarPorEstadoDebeRetornarCollectionModelConLinks() {
        when(pagoService.buscarPorEstado(EstadoPago.PENDIENTE)).thenReturn(List.of(response));

        ResponseEntity<CollectionModel<EntityModel<PagoResponse>>> resultado =
                pagoController.buscarPorEstado(EstadoPago.PENDIENTE);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getContent()).hasSize(1);
        assertThat(resultado.getBody().getLink("self")).isPresent();
        assertThat(resultado.getBody().getLink("todos")).isPresent();

        verify(pagoService).buscarPorEstado(EstadoPago.PENDIENTE);
    }

    @Test
    void eliminarPagoDebeRetornarNoContent() {
        doNothing().when(pagoService).eliminarPago(1L);

        ResponseEntity<Void> resultado = pagoController.eliminarPago(1L);

        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(resultado.getBody()).isNull();

        verify(pagoService).eliminarPago(1L);
    }
}
