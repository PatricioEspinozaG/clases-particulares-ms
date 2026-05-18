package com.classmate.claseservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClaseResponse {

    private Long id;
    private String asignatura;
    private String descripcion;
    private Double precio;
    private LocalDateTime fecha;
    private Integer duracion;
    private Long profesorId;

}
