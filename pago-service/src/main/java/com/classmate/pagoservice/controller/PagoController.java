package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crearPago(
            @Valid @RequestBody PagoRequest request) {

        PagoResponse response = pagoService.crearPago(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<PagoResponse> aprobarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(pagoService.aprobarPago(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<PagoResponse> rechazarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(pagoService.rechazarPago(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoResponse>> buscarPorEstado(
            @PathVariable EstadoPago estado) {

        return ResponseEntity.ok(pagoService.buscarPorEstado(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(
            @PathVariable Long id) {

        pagoService.eliminarPago(id);

        return ResponseEntity.noContent().build();
    }
}
