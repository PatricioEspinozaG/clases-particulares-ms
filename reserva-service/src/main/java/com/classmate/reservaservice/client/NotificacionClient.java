package com.classmate.reservaservice.client;

import com.classmate.reservaservice.dto.NotificacionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://notificacion-service:8087")
public interface NotificacionClient {

    @PostMapping("/notificaciones/reserva")
    String enviarNotificacionReserva(
            @RequestBody NotificacionRequest request);
}