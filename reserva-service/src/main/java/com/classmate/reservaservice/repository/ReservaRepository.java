package com.classmate.reservaservice.repository;

import com.classmate.reservaservice.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}