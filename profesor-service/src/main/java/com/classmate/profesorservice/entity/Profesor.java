package com.classmate.profesorservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "profesores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Long usuarioId;

    @Column(nullable = false)
    private String especialidad;

    private String descripcion;

    @Column(name = "precio_hora", nullable = false)
    private BigDecimal precioHora;

    @Column(name = "experiencia_anios")
    private Integer experienciaAnios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProfesor estado;
}