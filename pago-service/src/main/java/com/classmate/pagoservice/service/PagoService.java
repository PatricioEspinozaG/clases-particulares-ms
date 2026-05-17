package com.classmate.pagoservice.service;

import com.classmate.pagoservice.client.ReservaClient;
import com.classmate.pagoservice.dto.PagoRequest;
import com.classmate.pagoservice.dto.PagoResponse;
import com.classmate.pagoservice.entity.EstadoPago;
import com.classmate.pagoservice.entity.Pago;
import com.classmate.pagoservice.exception.ResourceNotFoundException;
import com.classmate.pagoservice.repository.PagoRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaClient reservaClient;

    public PagoService(PagoRepository pagoRepository, ReservaClient reservaClient) {
        this.pagoRepository = pagoRepository;
        this.reservaClient = reservaClient;
    }

    public PagoResponse crearPago(PagoRequest request) {

        log.info("Creando pago para reserva {}",
                request.getReservaId());

        validarReserva(request.getReservaId());

        Pago pago = new Pago();

        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());

        Pago guardado = pagoRepository.save(pago);

        log.info("Pago creado correctamente con id {}",
                guardado.getId());

        return toResponse(guardado);
    }

    public List<PagoResponse> obtenerPagos() {

        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PagoResponse obtenerPagoPorId(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        return toResponse(pago);
    }

    public PagoResponse aprobarPago(Long id) {

        log.info("Aprobando pago con id {}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        pago.setEstado(EstadoPago.APROBADO);

        log.info("Confirmando reserva {}",
                pago.getReservaId());

        reservaClient.confirmarReserva(
                pago.getReservaId());

        Pago actualizado = pagoRepository.save(pago);

        log.info("Pago {} aprobado correctamente",
                actualizado.getId());

        return toResponse(actualizado);
    }

    public PagoResponse rechazarPago(Long id) {

        log.info("Rechazando pago con id {}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));

        pago.setEstado(EstadoPago.RECHAZADO);

        Pago actualizado = pagoRepository.save(pago);

        log.info("Pago {} rechazado correctamente",
                actualizado.getId());

        return toResponse(actualizado);
    }

    public void eliminarPago(Long id) {

        log.info("Eliminando pago con id {}", id);

        if (!pagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pago no encontrado");
        }

        pagoRepository.deleteById(id);

        log.info("Pago {} eliminado correctamente", id);
    }

    public List<PagoResponse> buscarPorEstado(EstadoPago estado) {

        return pagoRepository.findByEstado(estado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarReserva(Long reservaId) {

        log.info("Validando reserva con id {}",
                reservaId);

        try {

            reservaClient.obtenerReservaPorId(reservaId);

        } catch (FeignException.NotFound e) {

            log.error("Reserva {} no existe",
                    reservaId);
            throw new ResourceNotFoundException(
                    "La reserva con id " + reservaId + " no existe");

        } catch (FeignException e) {

            log.error("Error conectando con reserva-service");

            throw new RuntimeException(
                    "No se puede conectar con reserva-service");
        }
    }

    private PagoResponse toResponse(Pago pago) {

        return new PagoResponse(
                pago.getId(),
                pago.getReservaId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstado(),
                pago.getFechaPago()
        );
    }
}
