package com.classmate.reservaservice.service;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.entity.EstadoReserva;
import com.classmate.reservaservice.entity.Reserva;
import com.classmate.reservaservice.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva crearReserva(CreateReservaRequest request) {

        if (request.getFechaReserva().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "No se puede reservar una fecha pasada");
        }

        boolean existeReserva = reservaRepository
                .existsByProfesorIdAndFechaReserva(
                        request.getProfesorId(),
                        request.getFechaReserva()
                );

        if (existeReserva) {
            throw new RuntimeException(
                    "El profesor ya tiene una reserva en ese horario");
        }

        Reserva reserva = new Reserva();

        reserva.setUsuarioId(request.getUsuarioId());
        reserva.setProfesorId(request.getProfesorId());
        reserva.setClaseId(request.getClaseId());
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(EstadoReserva.PENDIENTE);

        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerReservas() {

        return reservaRepository.findAll();
    }

    public Reserva obtenerReservaPorId(Long id) {

        return reservaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada"));
    }

    public Reserva cancelarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new RuntimeException("La reserva ya está cancelada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);

        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva no encontrada"));

        reservaRepository.delete(reserva);
    }
}