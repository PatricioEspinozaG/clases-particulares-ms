package com.classmate.profesorservice.controller;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Tag → agrupa todos los endpoints bajo "Profesores" en la UI de Swagger
@Tag(name = "Profesores", description = "Operaciones CRUD para gestión de profesores")
@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    // @Operation → describe qué hace el endpoint en Swagger
    // @ApiResponses → documenta los códigos HTTP posibles
    @Operation(summary = "Registrar un nuevo profesor",
               description = "Crea el perfil de profesor para un usuario existente. El usuarioId debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesor registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuarioId ya registrado como profesor")
    })
    @PostMapping
    public ResponseEntity<ProfesorResponse> crear(@Valid @RequestBody ProfesorRequest request) {
        ProfesorResponse response = profesorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los profesores",
               description = "Retorna la lista completa de profesores registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<ProfesorResponse>> listar() {
        return ResponseEntity.ok(profesorService.listar());
    }

    @Operation(summary = "Buscar profesor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponse> buscarPorId(
            // @Parameter → describe el parámetro en Swagger
            @Parameter(description = "ID del profesor a buscar") @PathVariable Long id) {
        return ResponseEntity.ok(profesorService.buscarPorId(id));
    }

    @Operation(summary = "Buscar profesor por ID de usuario",
               description = "Permite encontrar el perfil de profesor a partir del ID del usuario asociado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ProfesorResponse> buscarPorUsuarioId(
            @Parameter(description = "ID del usuario asociado al profesor") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(profesorService.buscarPorUsuarioId(usuarioId));
    }

    @Operation(summary = "Buscar profesores por especialidad",
               description = "Filtra profesores cuya especialidad contenga el texto indicado (sin distinción de mayúsculas).")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<ProfesorResponse>> buscarPorEspecialidad(
            @Parameter(description = "Texto a buscar dentro de la especialidad") @PathVariable String especialidad) {
        return ResponseEntity.ok(profesorService.buscarPorEspecialidad(especialidad));
    }

    @Operation(summary = "Actualizar un profesor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponse> actualizar(
            @Parameter(description = "ID del profesor a actualizar") @PathVariable Long id,
            @Valid @RequestBody ProfesorRequest request) {
        return ResponseEntity.ok(profesorService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un profesor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profesor eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del profesor a eliminar") @PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
