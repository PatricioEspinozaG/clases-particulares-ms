package com.classmate.pagoservice.client;

import com.classmate.pagoservice.dto.ReservaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

@FeignClient(name = "reserva-service", url = "http://localhost:8085")
public interface ReservaClient {

    @GetMapping("/reservas/{id}")
    ReservaResponse obtenerReservaPorId(@PathVariable Long id);

    @PatchMapping("/reservas/{id}/confirmar")
    ReservaResponse confirmarReserva(@PathVariable Long id);
}