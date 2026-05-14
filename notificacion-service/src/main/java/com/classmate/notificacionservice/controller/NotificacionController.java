package com.classmate.notificacionservice.controller;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(
            NotificacionService notificacionService) {

        this.notificacionService = notificacionService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> enviarEmail(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarEmail(request)
        );
    }

    @PostMapping("/pago")
    public ResponseEntity<String> enviarNotificacionPago(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarNotificacionPago(request)
        );
    }

    @PostMapping("/reserva")
    public ResponseEntity<String> enviarNotificacionReserva(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarNotificacionReserva(request)
        );
    }
}