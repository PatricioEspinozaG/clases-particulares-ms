package com.classmate.claseservice.controller;

import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.service.ClaseService;
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
@RequestMapping("/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<ClaseResponse>> crearClase(
            @Valid @RequestBody ClaseRequest request) {

        ClaseResponse response = claseService.crearClase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agregarLinks(response));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClaseResponse>>> obtenerClases() {

        List<EntityModel<ClaseResponse>> clases = claseService.obtenerClases()
                .stream()
                .map(this::agregarLinks)
                .toList();

        CollectionModel<EntityModel<ClaseResponse>> collection = CollectionModel.of(clases);

        collection.add(linkTo(methodOn(ClaseController.class)
                .obtenerClases()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClaseResponse>> actualizarClase(
            @PathVariable Long id,
            @Valid @RequestBody ClaseRequest request) {

        ClaseResponse claseActualizada = claseService.actualizarClase(id, request);

        return ResponseEntity.ok(agregarLinks(claseActualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClaseResponse>> obtenerClasePorId(
            @PathVariable Long id) {

        ClaseResponse response = claseService.obtenerClasePorId(id);

        return ResponseEntity.ok(agregarLinks(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarClase(@PathVariable Long id) {

        claseService.eliminarClase(id);

        return ResponseEntity.ok("Clase eliminada correctamente");
    }

    private EntityModel<ClaseResponse> agregarLinks(ClaseResponse response) {

        EntityModel<ClaseResponse> resource = EntityModel.of(response);

        resource.add(linkTo(methodOn(ClaseController.class)
                .obtenerClasePorId(response.getId())).withSelfRel());

        resource.add(linkTo(methodOn(ClaseController.class)
                .obtenerClases()).withRel("clases"));

        resource.add(linkTo(methodOn(ClaseController.class)
                .eliminarClase(response.getId())).withRel("eliminar"));

        return resource;
    }
}
