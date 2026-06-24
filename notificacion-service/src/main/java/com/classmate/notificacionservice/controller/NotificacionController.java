package com.classmate.notificacionservice.controller;

import com.classmate.notificacionservice.dto.NotificacionRequest;
import com.classmate.notificacionservice.dto.NotificacionResponse;
import com.classmate.notificacionservice.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Notificaciones",
        description = "Operaciones relacionadas con el envío y consulta de notificaciones"
)
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(
            NotificacionService notificacionService) {

        this.notificacionService = notificacionService;
    }

    @PostMapping("/email")
    @Operation(
            summary = "Enviar email",
            description = "Envía una notificación general por correo electrónico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar la notificación")
    })
    public ResponseEntity<String> enviarEmail(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarEmail(request)
        );
    }

    @PostMapping("/pago")
    @Operation(
            summary = "Enviar notificación de pago",
            description = "Envía una notificación relacionada con el estado o registro de un pago"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación de pago enviada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar la notificación")
    })
    public ResponseEntity<String> enviarNotificacionPago(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarNotificacionPago(request)
        );
    }

    @PostMapping("/reserva")
    @Operation(
            summary = "Enviar notificación de reserva",
            description = "Envía una notificación relacionada con la creación, confirmación o cancelación de una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación de reserva enviada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno al enviar la notificación")
    })
    public ResponseEntity<String> enviarNotificacionReserva(
            @Valid @RequestBody NotificacionRequest request) {

        return ResponseEntity.ok(
                notificacionService.enviarNotificacionReserva(request)
        );
    }

    @GetMapping
    @Operation(
            summary = "Listar notificaciones",
            description = "Obtiene todas las notificaciones registradas en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de notificaciones obtenido correctamente")
    })
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
