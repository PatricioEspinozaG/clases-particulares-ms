package com.classmate.profesorservice.dto;

import com.classmate.profesorservice.entity.EstadoProfesor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProfesorResponse {

    private Long id;
    private Long usuarioId;
    private String especialidad;
    private String descripcion;
    private BigDecimal precioHora;
    private Integer experienciaAnios;
    private EstadoProfesor estado;
}