package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pagos")
@Tag(
        name = "Pagos",
        description = "Operaciones relacionadas con la gestión de pagos de reservas"
)
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    @Operation(
            summary = "Crear pago",
            description = "Registra un nuevo pago asociado a una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un pago asociado a la reserva")
    })
    public ResponseEntity<EntityModel<PagoResponse>> crearPago(
            @Valid @RequestBody PagoRequest request) {

        PagoResponse response = pagoService.crearPago(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toModel(response));
    }

    @GetMapping
    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido correctamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponse>>> obtenerPagos() {

        List<EntityModel<PagoResponse>> pagos = pagoService.obtenerPagos()
                .stream()
                .map(this::toModel)
                .toList();

        CollectionModel<EntityModel<PagoResponse>> collection = CollectionModel.of(pagos);
        collection.add(linkTo(methodOn(PagoController.class).obtenerPagos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponse>> obtenerPagoPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.obtenerPagoPorId(id)));
    }

    @PutMapping("/{id}/aprobar")
    @Operation(
            summary = "Aprobar pago",
            description = "Aprueba un pago pendiente y actualiza su estado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago aprobado correctamente"),
            @ApiResponse(responseCode = "400", description = "El pago no puede ser aprobado por su estado actual"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponse>> aprobarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.aprobarPago(id)));
    }

    @PutMapping("/{id}/rechazar")
    @Operation(
            summary = "Rechazar pago",
            description = "Rechaza un pago pendiente y actualiza su estado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago rechazado correctamente"),
            @ApiResponse(responseCode = "400", description = "El pago no puede ser rechazado por su estado actual"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<EntityModel<PagoResponse>> rechazarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.rechazarPago(id)));
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Buscar pagos por estado",
            description = "Obtiene los pagos filtrados por estado, por ejemplo PENDIENTE, APROBADO o RECHAZADO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Estado de pago inválido")
    })
    public ResponseEntity<CollectionModel<EntityModel<PagoResponse>>> buscarPorEstado(
            @PathVariable EstadoPago estado) {

        List<EntityModel<PagoResponse>> pagos = pagoService.buscarPorEstado(estado)
                .stream()
                .map(this::toModel)
                .toList();

        CollectionModel<EntityModel<PagoResponse>> collection = CollectionModel.of(pagos);
        collection.add(linkTo(methodOn(PagoController.class).buscarPorEstado(estado)).withSelfRel());
        collection.add(linkTo(methodOn(PagoController.class).obtenerPagos()).withRel("todos"));

        return ResponseEntity.ok(collection);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar pago",
            description = "Elimina un pago del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @PathVariable Long id) {

        pagoService.eliminarPago(id);

        return ResponseEntity.noContent().build();
    }

    private EntityModel<PagoResponse> toModel(PagoResponse response) {

        EntityModel<PagoResponse> resource = EntityModel.of(response);

        resource.add(linkTo(methodOn(PagoController.class)
                .obtenerPagoPorId(response.getId())).withSelfRel());

        resource.add(linkTo(methodOn(PagoController.class)
                .obtenerPagos()).withRel("pagos"));

        resource.add(linkTo(methodOn(PagoController.class)
                .buscarPorEstado(response.getEstado())).withRel("buscar-por-estado"));

        if (response.getEstado() == EstadoPago.PENDIENTE) {
            resource.add(linkTo(methodOn(PagoController.class)
                    .aprobarPago(response.getId())).withRel("aprobar"));

            resource.add(linkTo(methodOn(PagoController.class)
                    .rechazarPago(response.getId())).withRel("rechazar"));
        }

        return resource;
    }
}
