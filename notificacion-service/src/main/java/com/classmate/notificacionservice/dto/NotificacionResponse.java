package com.classmate.notificacionservice.dto;

import java.time.LocalDateTime;

public class NotificacionResponse {

    private Long id;
    private String destinatario;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    public NotificacionResponse(Long id,
                                String destinatario,
                                String mensaje,
                                LocalDateTime fechaEnvio) {

        this.id = id;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
}