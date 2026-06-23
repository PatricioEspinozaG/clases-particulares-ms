package com.classmate.claseservice.controller;

import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.service.ClaseService;
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
@RequestMapping("/clases")
@Tag(
        name = "Clases",
        description = "Operaciones relacionadas con la gestión de clases particulares"
)
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @PostMapping
    @Operation(
            summary = "Crear clase",
            description = "Registra una nueva clase particular en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clase creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "409", description = "Conflicto con una clase ya existente")
    })
    public ResponseEntity<EntityModel<ClaseResponse>> crearClase(
            @Valid @RequestBody ClaseRequest request) {

        ClaseResponse response = claseService.crearClase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agregarLinks(response));
    }

    @GetMapping
    @Operation(
            summary = "Listar clases",
            description = "Obtiene todas las clases particulares registradas en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de clases obtenido correctamente")
    })
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

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar clase por ID",
            description = "Obtiene una clase particular mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase encontrada"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    public ResponseEntity<EntityModel<ClaseResponse>> obtenerClasePorId(
            @PathVariable Long id) {

        ClaseResponse response = claseService.obtenerClasePorId(id);

        return ResponseEntity.ok(agregarLinks(response));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar clase",
            description = "Actualiza los datos de una clase particular existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de validación"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    public ResponseEntity<EntityModel<ClaseResponse>> actualizarClase(
            @PathVariable Long id,
            @Valid @RequestBody ClaseRequest request) {

        ClaseResponse claseActualizada = claseService.actualizarClase(id, request);

        return ResponseEntity.ok(agregarLinks(claseActualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar clase",
            description = "Elimina una clase particular del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
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
                .actualizarClase(response.getId(), null)).withRel("actualizar"));

        resource.add(linkTo(methodOn(ClaseController.class)
                .eliminarClase(response.getId())).withRel("eliminar"));

        return resource;
    }
}
