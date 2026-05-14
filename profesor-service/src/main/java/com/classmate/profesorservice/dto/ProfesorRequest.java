package com.classmate.profesorservice.dto;

import com.classmate.profesorservice.entity.EstadoProfesor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfesorRequest {

    @NotNull
    private Long usuarioId;

    @NotBlank
    private String especialidad;

    private String descripcion;

    @NotNull
    @Positive
    private BigDecimal precioHora;

    private Integer experienciaAnios;

    @NotNull
    private EstadoProfesor estado;
}