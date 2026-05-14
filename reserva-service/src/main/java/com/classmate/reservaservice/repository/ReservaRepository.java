package com.classmate.reservaservice.repository;

import com.classmate.reservaservice.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByProfesorIdAndFechaReserva(
            Long profesorId,
            LocalDateTime fechaReserva
    );
}