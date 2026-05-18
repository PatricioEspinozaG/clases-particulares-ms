package com.classmate.reservaservice.dto;

import lombok.Data;

@Data
public class NotificacionRequest {

    private String destinatario;
    private String asunto;
    private String mensaje;
}