package com.classmate.pagoservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoRequest {

    @NotNull
    private Long reservaId;

    @NotNull
    @Positive
    private Double monto;

    @NotBlank
    private String metodoPago;
}
