package com.classmate.reservaservice.service;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.entity.EstadoReserva;
import com.classmate.reservaservice.entity.Reserva;
import com.classmate.reservaservice.repository.ReservaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva crearReserva(CreateReservaRequest request) {

        Reserva reserva = new Reserva();

        reserva.setUsuarioId(request.getUsuarioId());
        reserva.setProfesorId(request.getProfesorId());
        reserva.setClaseId(request.getClaseId());
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(EstadoReserva.PENDIENTE);

        return reservaRepository.save(reserva);
    }
}