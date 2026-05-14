package com.classmate.reservaservice.controller;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
import com.classmate.reservaservice.entity.Reserva;
import com.classmate.reservaservice.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody CreateReservaRequest request) {

        Reserva reserva = reservaService.crearReserva(request);

        ReservaResponse response = new ReservaResponse(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getProfesorId(),
                reserva.getClaseId(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> obtenerReservas() {

        List<ReservaResponse> reservas = reservaService
                .obtenerReservas()
                .stream()
                .map(reserva -> new ReservaResponse(
                        reserva.getId(),
                        reserva.getUsuarioId(),
                        reserva.getProfesorId(),
                        reserva.getClaseId(),
                        reserva.getFechaReserva(),
                        reserva.getEstado()
                ))
                .toList();

        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerReservaPorId(
            @PathVariable Long id) {

        Reserva reserva = reservaService.obtenerReservaPorId(id);

        ReservaResponse response = new ReservaResponse(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getProfesorId(),
                reserva.getClaseId(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponse> cancelarReserva(
            @PathVariable Long id) {

        Reserva reserva = reservaService.cancelarReserva(id);

        ReservaResponse response = new ReservaResponse(
                reserva.getId(),
                reserva.getUsuarioId(),
                reserva.getProfesorId(),
                reserva.getClaseId(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable Long id) {

        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }
}