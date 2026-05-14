package com.classmate.notificacionservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinatario;

    private String mensaje;

    private LocalDateTime fechaEnvio;
}