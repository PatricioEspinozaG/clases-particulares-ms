package com.classmate.pagoservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaResponse {

    private Long id;
    private Long usuarioId;
    private Long profesorId;
    private Long claseId;
    private LocalDateTime fechaReserva;
    private String estado;
}