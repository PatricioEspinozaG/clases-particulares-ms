package com.classmate.pagoservice.dto;

import lombok.Data;

@Data
public class NotificacionRequest {

    private String destinatario;
    private String asunto;
    private String mensaje;
}