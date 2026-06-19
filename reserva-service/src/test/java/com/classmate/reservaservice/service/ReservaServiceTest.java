package com.classmate.reservaservice.service;

import com.classmate.reservaservice.client.ClaseClient;
import com.classmate.reservaservice.client.NotificacionClient;
import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.NotificacionRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
import com.classmate.reservaservice.entity.EstadoReserva;
import com.classmate.reservaservice.entity.Reserva;
import com.classmate.reservaservice.exception.ResourceNotFoundException;
import com.classmate.reservaservice.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClaseClient claseClient;

    @Mock
    private NotificacionClient notificacionClient;

    @Test
    public void testCrearReservaExitoso() {

        CreateReservaRequest request =
                new CreateReservaRequest();

        request.setUsuarioId(1L);
        request.setProfesorId(2L);
        request.setClaseId(3L);
        request.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );

        when(claseClient.obtenerClasePorId(3L))
                .thenReturn(new Object());

        when(
                reservaRepository.existsByProfesorIdAndFechaReserva(
                        request.getProfesorId(),
                        request.getFechaReserva()
                )
        ).thenReturn(false);

        Reserva reservaGuardada = new Reserva();

        reservaGuardada.setId(1L);
        reservaGuardada.setUsuarioId(1L);
        reservaGuardada.setProfesorId(2L);
        reservaGuardada.setClaseId(3L);
        reservaGuardada.setFechaReserva(
                request.getFechaReserva()
        );
        reservaGuardada.setEstado(
                EstadoReserva.PENDIENTE
        );

        when(reservaRepository.save(any(Reserva.class)))
                .thenReturn(reservaGuardada);

        when(notificacionClient.enviarNotificacionReserva(
                any(NotificacionRequest.class)
        )).thenReturn("Notificación enviada");

        ReservaResponse response =
                reservaService.crearReserva(request);

        assertNotNull(response);

        assertEquals(
                EstadoReserva.PENDIENTE,
                response.getEstado()
        );

        assertEquals(
                1L,
                response.getUsuarioId()
        );

        verify(notificacionClient, times(1))
                .enviarNotificacionReserva(any());

        verify(reservaRepository, times(1))
                .save(any(Reserva.class));
    }

    @Test
    public void testCrearReservaFechaPasada() {

        CreateReservaRequest request =
                new CreateReservaRequest();

        request.setUsuarioId(1L);
        request.setProfesorId(2L);
        request.setClaseId(3L);

        request.setFechaReserva(
                LocalDateTime.now().minusDays(1)
        );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> reservaService.crearReserva(request)
                );

        assertEquals(
                "No se puede reservar una fecha pasada",
                exception.getMessage()
        );

        verify(reservaRepository, never())
                .save(any());
    }

    @Test
    public void testCrearReservaProfesorOcupado() {

        CreateReservaRequest request =
                new CreateReservaRequest();

        request.setUsuarioId(1L);
        request.setProfesorId(2L);
        request.setClaseId(3L);

        request.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );

        when(claseClient.obtenerClasePorId(3L))
                .thenReturn(new Object());

        when(
                reservaRepository.existsByProfesorIdAndFechaReserva(
                        request.getProfesorId(),
                        request.getFechaReserva()
                )
        ).thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> reservaService.crearReserva(request)
                );

        assertEquals(
                "El profesor ya tiene una reserva en ese horario",
                exception.getMessage()
        );

        verify(reservaRepository, never())
                .save(any());
    }

    @Test
    public void testObtenerReservaPorId() {

        Reserva reserva = new Reserva();

        reserva.setId(1L);
        reserva.setUsuarioId(1L);
        reserva.setProfesorId(2L);
        reserva.setClaseId(3L);

        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );

        reserva.setEstado(
                EstadoReserva.PENDIENTE
        );

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reserva));

        ReservaResponse response =
                reservaService.obtenerReservaPorId(1L);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                EstadoReserva.PENDIENTE,
                response.getEstado()
        );
    }

    @Test
    public void testObtenerReservaPorIdNoExiste() {

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> reservaService.obtenerReservaPorId(1L)
                );

        assertEquals(
                "Reserva no encontrada",
                exception.getMessage()
        );
    }

    @Test
    public void testCancelarReserva() {

        Reserva reserva = new Reserva();

        reserva.setId(1L);
        reserva.setUsuarioId(1L);
        reserva.setProfesorId(2L);
        reserva.setClaseId(3L);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );
        reserva.setEstado(EstadoReserva.PENDIENTE);

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reserva));

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservaResponse response =
                reservaService.cancelarReserva(1L);

        assertNotNull(response);

        assertEquals(
                EstadoReserva.CANCELADA,
                response.getEstado()
        );

        verify(reservaRepository, times(1))
                .save(any(Reserva.class));
    }

    @Test
    public void testConfirmarReserva() {

        Reserva reserva = new Reserva();

        reserva.setId(1L);
        reserva.setUsuarioId(1L);
        reserva.setProfesorId(2L);
        reserva.setClaseId(3L);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1)
        );
        reserva.setEstado(EstadoReserva.PENDIENTE);

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reserva));

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservaResponse response =
                reservaService.confirmarReserva(1L);

        assertNotNull(response);

        assertEquals(
                EstadoReserva.CONFIRMADA,
                response.getEstado()
        );

        verify(reservaRepository, times(1))
                .save(any(Reserva.class));
    }
}