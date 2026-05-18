package com.classmate.claseservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaseRequest {

    @NotBlank
    private String asignatura;

    @NotBlank
    private String descripcion;

    @NotNull
    @Positive
    private Double precio;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    @Positive
    private Integer duracion;

    @NotNull
    private Long profesorId;
}
