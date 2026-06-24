package com.classmate.pagoservice.client;

import com.classmate.pagoservice.dto.ReservaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "reserva-service")
public interface ReservaClient {

    @GetMapping("/reservas/{id}")
    ReservaResponse obtenerReservaPorId(@PathVariable Long id);

    @PutMapping("/reservas/{id}/confirmar")
    ReservaResponse confirmarReserva(@PathVariable Long id);
}