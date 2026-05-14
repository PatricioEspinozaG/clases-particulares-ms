package com.classmate.reservaservice.controller;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> obtenerReservas() {

        return ResponseEntity.ok(
                reservaService.obtenerReservas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerReservaPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.obtenerReservaPorId(id)
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponse> cancelarReserva(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.cancelarReserva(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable Long id) {

        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }
}