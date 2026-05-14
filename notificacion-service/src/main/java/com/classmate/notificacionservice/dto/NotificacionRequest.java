package com.classmate.notificacionservice.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class NotificacionRequest {

    @Email
    @NotBlank
    private String destinatario;

    @NotBlank
    private String asunto;

    @NotBlank
    private String mensaje;
}
