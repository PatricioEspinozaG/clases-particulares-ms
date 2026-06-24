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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/profesores")
@Tag(
        name = "Profesores",
        description = "Operaciones relacionadas con la gestión de profesores del sistema"
)
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar profesor",
            description = "Crea el perfil de profesor para un usuario existente. El usuarioId debe ser único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesor registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Usuario ya registrado como profesor o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en usuario-service")
    })
    public ResponseEntity<ProfesorResponse> crear(
            @Valid @RequestBody ProfesorRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profesorService.crear(request));
    }

    @GetMapping
    @Operation(
            summary = "Listar profesores",
            description = "Obtiene todos los profesores registrados en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public ResponseEntity<List<ProfesorResponse>> listar() {
        return ResponseEntity.ok(profesorService.listar());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar profesor por ID",
            description = "Obtiene un profesor mediante su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<ProfesorResponse> buscarPorId(
            @PathVariable Long id) {

        ProfesorResponse response = profesorService.buscarPorId(id);

        // HATEOAS: agrega link "self" apuntando a este mismo endpoint
        response.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .buscarPorId(id)
                ).withSelfRel()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Buscar profesor por usuarioId",
            description = "Permite encontrar el perfil de profesor a partir del ID del usuario asociado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<ProfesorResponse> buscarPorUsuarioId(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                profesorService.buscarPorUsuarioId(usuarioId)
        );
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(
            summary = "Buscar profesores por especialidad",
            description = "Filtra profesores cuya especialidad contenga el texto indicado (sin distinción de mayúsculas)"
    )
    @ApiResponse(responseCode = "200", description = "Lista filtrada correctamente")
    public ResponseEntity<List<ProfesorResponse>> buscarPorEspecialidad(
            @PathVariable String especialidad) {

        return ResponseEntity.ok(
                profesorService.buscarPorEspecialidad(especialidad)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar profesor",
            description = "Actualiza los datos de un profesor existente"
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
            @ApiResponse(responseCode = "204", description = "Profesor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
