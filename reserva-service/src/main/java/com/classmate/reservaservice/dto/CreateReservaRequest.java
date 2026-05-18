package com.classmate.reservaservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservaRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long profesorId;

    @NotNull
    private Long claseId;

    @NotNull
    private LocalDateTime fechaReserva;
}