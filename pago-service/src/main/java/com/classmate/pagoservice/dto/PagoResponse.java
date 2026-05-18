package com.classmate.pagoservice.dto;

import com.classmate.pagoservice.entity.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PagoResponse {

    private Long id;
    private Long reservaId;
    private Double monto;
    private String metodoPago;
    private EstadoPago estado;
    private LocalDateTime fechaPago;
}
