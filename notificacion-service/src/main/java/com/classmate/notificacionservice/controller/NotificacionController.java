package com.classmate.notificacionservice.controller;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.dto.NotificacionResponse;
import com.classmate.notificacionservice.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<NotificacionResponse>>> obtenerNotificaciones() {

        List<EntityModel<NotificacionResponse>> notificaciones = notificacionService.obtenerNotificaciones()
                .stream()
                .map(this::toEntityModel)
                .toList();

        CollectionModel<EntityModel<NotificacionResponse>> collectionModel = CollectionModel.of(
                notificaciones,
                linkTo(methodOn(NotificacionController.class).obtenerNotificaciones()).withSelfRel(),
                linkTo(methodOn(NotificacionController.class).enviarEmail(null)).withRel("enviar-email"),
                linkTo(methodOn(NotificacionController.class).enviarNotificacionPago(null)).withRel("notificar-pago"),
                linkTo(methodOn(NotificacionController.class).enviarNotificacionReserva(null)).withRel("notificar-reserva")
        );

        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<NotificacionResponse> toEntityModel(NotificacionResponse response) {

        return EntityModel.of(
                response,
                linkTo(methodOn(NotificacionController.class).obtenerNotificaciones()).withRel("notificaciones")
        );
    }
}
