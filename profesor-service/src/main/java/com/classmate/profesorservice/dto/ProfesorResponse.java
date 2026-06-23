package com.classmate.profesorservice.dto;

import com.classmate.profesorservice.entity.EstadoProfesor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
// RepresentationModel permite agregar links HATEOAS a la respuesta
public class ProfesorResponse extends RepresentationModel<ProfesorResponse> {

    private Long id;
    private Long usuarioId;
    private String especialidad;
    private String descripcion;
    private BigDecimal precioHora;
    private Integer experienciaAnios;
    private EstadoProfesor estado;
}
