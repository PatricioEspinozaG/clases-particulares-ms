package com.classmate.profesorservice.controller;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@Tag(name= "Profesores", description = "Operaciones relacionadas con la gestión de profesores")
//http://localhost:8083/doc/swagger-ui/index.html#/
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @PostMapping
    @Operation(
            summary = "Crear profesor",
            description = "Registra un nuevo profesor en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesor creado correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ProfesorResponse> crear(@Valid @RequestBody ProfesorRequest request) {
        ProfesorResponse response = profesorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar profesores",
            description = "Obtiene todos los profesores registrados"
    )
    public ResponseEntity<List<ProfesorResponse>> listar() {
        return ResponseEntity.ok(profesorService.listar());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar profesor por ID",
            description = "Obtiene un profesor mediante su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<ProfesorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Buscar profesor por usuario",
            description = "Obtiene un profesor mediante el ID del usuario asociado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<ProfesorResponse> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(profesorService.buscarPorUsuarioId(usuarioId));
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(
            summary = "Buscar profesores por especialidad",
            description = "Obtiene una lista de profesores filtrados por especialidad"
    )
    public ResponseEntity<List<ProfesorResponse>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(profesorService.buscarPorEspecialidad(especialidad));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar profesor",
            description = "Actualiza la información de un profesor existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<ProfesorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesorRequest request) {

        return ResponseEntity.ok(profesorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar profesor",
            description = "Elimina un profesor del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<String> eliminar(@PathVariable Long id) {

        profesorService.eliminar(id);

        return ResponseEntity.ok("Profesor eliminado correctamente");
    }
}