package com.classmate.reservaservice.controller;

import com.classmate.reservaservice.dto.CreateReservaRequest;
import com.classmate.reservaservice.dto.ReservaResponse;
import com.classmate.reservaservice.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@Tag(
        name = "Reservas",
        description = "Operaciones relacionadas con la gestión de reservas de clases"
)
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(
            summary = "Crear reserva",
            description = "Registra una nueva reserva de clase para un estudiante"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, profesor ocupado o error de validación"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody CreateReservaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @GetMapping
    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas en el sistema"
    )
    public ResponseEntity<List<ReservaResponse>> obtenerReservas() {

        return ResponseEntity.ok(
                reservaService.obtenerReservas()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponse> obtenerReservaPorId(
            @PathVariable Long id) {

        //Esto es del HATEOAS
        ReservaResponse response =
                reservaService.obtenerReservaPorId(id);
        response.add(
                linkTo(
                        methodOn(ReservaController.class)
                                .obtenerReservaPorId(id)
                ).withSelfRel()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela una reserva existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
            @ApiResponse(responseCode = "400", description = "La reserva ya está cancelada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponse> cancelarReserva(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservaService.cancelarReserva(id)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable Long id) {

        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmar")
    @Operation(
            summary = "Confirmar reserva",
            description = "Confirma una reserva luego de completar el proceso de pago"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva confirmada correctamente"),
            @ApiResponse(responseCode = "400", description = "No se puede confirmar una reserva cancelada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponse> confirmarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.confirmarReserva(id));
    }
}