package com.classmate.reservaservice.dto;

import com.classmate.reservaservice.entity.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservaResponse extends RepresentationModel<ReservaResponse> {

    private Long id;
    private Long usuarioId;
    private Long profesorId;
    private Long claseId;
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;
}