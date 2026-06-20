package com.classmate.pagoservice.controller;

import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.service.PagoService;
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
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<PagoResponse>> crearPago(
            @Valid @RequestBody PagoRequest request) {

        PagoResponse response = pagoService.crearPago(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toModel(response));
    }

    @GetMapping
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
    public ResponseEntity<EntityModel<PagoResponse>> obtenerPagoPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.obtenerPagoPorId(id)));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<EntityModel<PagoResponse>> aprobarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.aprobarPago(id)));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<EntityModel<PagoResponse>> rechazarPago(
            @PathVariable Long id) {

        return ResponseEntity.ok(toModel(pagoService.rechazarPago(id)));
    }

    @GetMapping("/estado/{estado}")
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
