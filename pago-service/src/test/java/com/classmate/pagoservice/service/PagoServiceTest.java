package com.classmate.pagoservice.service;

import com.classmate.pagoservice.client.NotificacionClient;
import com.classmate.pagoservice.client.ReservaClient;
import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.dto.ReservaResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.exception.ResourceNotFoundException;
import com.classmate.pagoservice.repository.PagoRepository;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private NotificacionClient notificacionClient;

    @InjectMocks
    private PagoService pagoService;

    private final Faker faker = new Faker();
    private Pago pago;
    private PagoRequest request;
    private ReservaResponse reservaResponse;

    @BeforeEach
    void setUp() {
        request = new PagoRequest();
        request.setReservaId(faker.number().numberBetween(1L, 100L));
        request.setMonto(faker.number().randomDouble(2, 10000, 90000));
        request.setMetodoPago("TARJETA");

        pago = new Pago();
        pago.setId(faker.number().numberBetween(1L, 100L));
        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());

        reservaResponse = new ReservaResponse();
        reservaResponse.setId(request.getReservaId());
        reservaResponse.setEstado("PENDIENTE");
    }

    @Test
    void crearPago_DeberiaGuardarPagoPendiente_CuandoReservaExiste() {
        when(reservaClient.obtenerReservaPorId(request.getReservaId())).thenReturn(reservaResponse);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponse response = pagoService.crearPago(request);

        assertNotNull(response);
        assertEquals(pago.getId(), response.getId());
        assertEquals(EstadoPago.PENDIENTE, response.getEstado());
        assertEquals(request.getReservaId(), response.getReservaId());
        verify(reservaClient).obtenerReservaPorId(request.getReservaId());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void obtenerPagoPorId_DeberiaRetornarPago_CuandoExiste() {
        when(pagoRepository.findById(pago.getId())).thenReturn(Optional.of(pago));

        PagoResponse response = pagoService.obtenerPagoPorId(pago.getId());

        assertEquals(pago.getId(), response.getId());
        assertEquals(pago.getReservaId(), response.getReservaId());
        verify(pagoRepository).findById(pago.getId());
    }

    @Test
    void obtenerPagoPorId_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pagoService.obtenerPagoPorId(999L));
    }

    @Test
    void obtenerPagos_DeberiaRetornarListaDePagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<PagoResponse> response = pagoService.obtenerPagos();

        assertEquals(1, response.size());
        assertEquals(pago.getId(), response.get(0).getId());
    }

    @Test
    void aprobarPago_DeberiaCambiarEstadoYConfirmarReserva() {
        when(pagoRepository.findById(pago.getId())).thenReturn(Optional.of(pago));
        when(reservaClient.confirmarReserva(pago.getReservaId())).thenReturn(reservaResponse);
        when(notificacionClient.enviarNotificacionPago(any())).thenReturn("Notificación enviada");
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoResponse response = pagoService.aprobarPago(pago.getId());

        assertEquals(EstadoPago.APROBADO, response.getEstado());
        verify(reservaClient).confirmarReserva(pago.getReservaId());
        verify(notificacionClient).enviarNotificacionPago(any());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void rechazarPago_DeberiaCambiarEstadoYEnviarNotificacion() {
        when(pagoRepository.findById(pago.getId())).thenReturn(Optional.of(pago));
        when(notificacionClient.enviarNotificacionPago(any())).thenReturn("Notificación enviada");
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoResponse response = pagoService.rechazarPago(pago.getId());

        assertEquals(EstadoPago.RECHAZADO, response.getEstado());
        verify(notificacionClient).enviarNotificacionPago(any());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void eliminarPago_DeberiaEliminar_CuandoExiste() {
        when(pagoRepository.existsById(pago.getId())).thenReturn(true);

        pagoService.eliminarPago(pago.getId());

        verify(pagoRepository).deleteById(pago.getId());
    }

    @Test
    void eliminarPago_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(pagoRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> pagoService.eliminarPago(999L));

        verify(pagoRepository, never()).deleteById(999L);
    }

    @Test
    void buscarPorEstado_DeberiaRetornarPagosFiltrados() {
        when(pagoRepository.findByEstado(EstadoPago.PENDIENTE)).thenReturn(List.of(pago));

        List<PagoResponse> response = pagoService.buscarPorEstado(EstadoPago.PENDIENTE);

        assertEquals(1, response.size());
        assertEquals(EstadoPago.PENDIENTE, response.get(0).getEstado());
    }
}
