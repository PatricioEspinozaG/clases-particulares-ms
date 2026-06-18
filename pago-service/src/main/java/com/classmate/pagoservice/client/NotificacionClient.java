package com.classmate.pagoservice.client;

import com.classmate.pagoservice.dto.NotificacionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacion-service")
public interface NotificacionClient {

    @PostMapping("/notificaciones/pago")
    String enviarNotificacionPago(
            @RequestBody NotificacionRequest request);
}