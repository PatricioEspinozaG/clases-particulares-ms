package com.classmate.claseservice.controller;


import com.classmate.claseservice.dto.ClaseRequest;
import com.classmate.claseservice.dto.ClaseResponse;
import com.classmate.claseservice.service.ClaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService){
        this.claseService = claseService;
    }

    @PostMapping
    public ResponseEntity<ClaseResponse> crearClase(
            @Valid @RequestBody ClaseRequest request){
        ClaseResponse response = claseService.crearClase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClaseResponse>> obtenerClases(){
        return ResponseEntity.ok(claseService.obtenerClases());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaseResponse> actualizarClase(
            @PathVariable Long id, @Valid @RequestBody ClaseRequest request){

        ClaseResponse  claseActualizada = claseService.actualizarClase(id, request);

        return ResponseEntity.ok(claseActualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseResponse> obtenerClasePorId(
            @PathVariable Long id){

        return ResponseEntity.ok(claseService.obtenerClasePorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarClase(@PathVariable Long id){

        claseService.eliminarClase(id);

        return ResponseEntity.ok("Clase eliminada correctamente");
    }
}