package com.classmate.profesorservice.controller;

import com.classmate.profesorservice.dto.ProfesorRequest;
import com.classmate.profesorservice.dto.ProfesorResponse;
import com.classmate.profesorservice.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @PostMapping
    public ResponseEntity<ProfesorResponse> crear(@Valid @RequestBody ProfesorRequest request) {
        ProfesorResponse response = profesorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfesorResponse>> listar() {
        return ResponseEntity.ok(profesorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ProfesorResponse> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(profesorService.buscarPorUsuarioId(usuarioId));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<ProfesorResponse>> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(profesorService.buscarPorEspecialidad(especialidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesorRequest request) {

        return ResponseEntity.ok(profesorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}