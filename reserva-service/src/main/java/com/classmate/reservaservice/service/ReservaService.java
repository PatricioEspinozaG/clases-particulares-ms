package com.classmate.reservaservice.service;
import com.classmate.reservaservice.client.ClaseClient;
import com.classmate.reservaservice.client.NotificacionClient;
import com.classmate.reservaservice.dto.NotificacionRequest;
import com.classmate.reservaservice.exception.ResourceNotFoundException;
import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
import com.classmate.reservaservice.entity.EstadoReserva;
import com.classmate.reservaservice.entity.Reserva;
import com.classmate.reservaservice.repository.ReservaRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseClient claseClient;
    private final NotificacionClient notificacionClient;

    public ReservaService(ReservaRepository reservaRepository,
                          ClaseClient claseClient,
                          NotificacionClient notificacionClient) {
        this.reservaRepository = reservaRepository;
        this.claseClient = claseClient;
        this.notificacionClient = notificacionClient;
    }

    public ReservaResponse crearReserva(CreateReservaRequest request) {

        log.info("Creando reserva para profesor {} y usuario {}",
                request.getProfesorId(),
                request.getUsuarioId());

        if (request.getFechaReserva().isBefore(LocalDateTime.now())) {
            log.error("Intento de reserva con fecha pasada");
            throw new RuntimeException(
                    "No se puede reservar una fecha pasada");
        }

        validarClase(request.getClaseId());
        boolean existeReserva = reservaRepository
                .existsByProfesorIdAndFechaReserva(
                        request.getProfesorId(),
                        request.getFechaReserva()
                );

        if (existeReserva) {
            log.error("El profesor {} ya tiene reserva en {}",
                    request.getProfesorId(),
                    request.getFechaReserva());
            throw new RuntimeException(
                    "El profesor ya tiene una reserva en ese horario");
        }

        Reserva reserva = new Reserva();

        reserva.setUsuarioId(request.getUsuarioId());
        reserva.setProfesorId(request.getProfesorId());
        reserva.setClaseId(request.getClaseId());
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(EstadoReserva.PENDIENTE);

        Reserva reservaGuardada = reservaRepository.save(reserva);

        log.info("Reserva creada correctamente con id {}",
                reservaGuardada.getId());

        NotificacionRequest notificacionRequest =
                new NotificacionRequest();

        notificacionRequest.setDestinatario(
                "usuario@classmate.com");

        notificacionRequest.setAsunto(
                "Reserva creada");

        notificacionRequest.setMensaje(
                "Tu reserva fue creada correctamente");

        log.info("Enviando notificación de reserva");

        notificacionClient.enviarNotificacionReserva(
                notificacionRequest);

        log.info("Notificación de reserva enviada correctamente");

        return toResponse(reservaGuardada);
    }

    public List<ReservaResponse> obtenerReservas() {

        return reservaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReservaResponse obtenerReservaPorId(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada"));

        return toResponse(reserva);
    }

    public ReservaResponse cancelarReserva(Long id) {

        log.info("Cancelando reserva con id {}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            log.error("La reserva {} ya estaba cancelada", id);
            throw new RuntimeException(
                    "La reserva ya está cancelada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);

        Reserva reservaActualizada = reservaRepository.save(reserva);

        log.info("Reserva {} cancelada correctamente",
                reservaActualizada.getId());

        return toResponse(reservaActualizada);
    }

    public void eliminarReserva(Long id) {

        log.info("Eliminando reserva con id {}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada"));

        reservaRepository.delete(reserva);

        log.info("Reserva {} eliminada correctamente", id);

    }

    public ReservaResponse confirmarReserva(Long id) {

        log.info("Confirmando reserva con id {}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            log.error("No se puede confirmar reserva cancelada {}", id);
            throw new RuntimeException("No se puede confirmar una reserva cancelada");
        }

        reserva.setEstado(EstadoReserva.CONFIRMADA);

        Reserva reservaActualizada = reservaRepository.save(reserva);

        log.info("Reserva {} confirmada correctamente",
                reservaActualizada.getId());

        return toResponse(reservaActualizada);
    }

    private void validarClase(Long claseId) {

        log.info("Validando clase con id {}",
                claseId);

        try {

            claseClient.obtenerClasePorId(claseId);

        } catch (FeignException.NotFound e) {

            log.error("Clase {} no existe",
                    claseId);

            throw new ResourceNotFoundException(
                    "La clase con id " + claseId + " no existe");

        } catch (FeignException e) {

            log.error("Error conectando con clase-service");

            throw new RuntimeException(
                    "No se puede conectar con clase-service");
        }
    }

    private ReservaResponse toResponse(Reserva reserva) {

        return new ReservaResponse(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getProfesorId(),
                reserva.getClaseId(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );
    }
    
}